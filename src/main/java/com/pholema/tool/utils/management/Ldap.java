package com.pholema.tool.utils.management;

import org.apache.log4j.Logger;

import com.pholema.tool.utils.common.StringUtils;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ldap {

    private static Logger logger = Logger.getLogger(Ldap.class);

	private final static String username = "root";
	private final static String password = "1234";

	private static DirContext GetContext(String username, String password) throws NamingException {
		Hashtable<String,String> env = new Hashtable<>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:389/");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "abs_corp\\" + username);
		env.put(Context.SECURITY_CREDENTIALS, password);
		return new InitialDirContext(env);
	}
	
	private static Map<String,String> GetDN(DirContext context, String username) throws NamingException {
	    SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    String baseDN = "DC=buyabs,DC=corp";
	    // dh78 2018.3.28	add search range fuzzy
	    String searchFilter = "(&(displayName=*)(objectClass=person)(|(sAMAccountName="+username+"*)(cn="+username+"*)))";
	    String dn;
	    NamingEnumeration<SearchResult> answers = context.search(baseDN, searchFilter, constraints);
		Map<String,String> DNMap = new HashMap<>();
	    while (answers.hasMoreElements()) {
            SearchResult result = answers.next();
            dn = result.getNameInNamespace();
			fixDN(dn, DNMap);
			logger.debug("dn " + dn);
        }
        return DNMap;
	}

	private static boolean matchPerson(boolean includeDisabledAccount, String dn) {
		return dn.toLowerCase().contains("ou=users") || (includeDisabledAccount && dn.toLowerCase().contains("ou=disabled accounts"));
	}

	/**
	 * avoid cn
	 */
	private static String fixDN(String source, Map<String, String> map) {
		//"CN=Marco.R.Ma,OU=MIS,OU=Users,OU=CD02,OU=Sichuan,OU=CN,DC=buyabs,DC=corp"
		String[] ar = source.split(",");
		String r = "";
		for (int j = 1; j< ar.length; j++) {
			r += (r.equals("")) ? ar[j] : ("," + ar[j]);
		}

		map.put(ar[0].substring(3), r);
		return r;
	}
	
	private static String GetAttributes(DirContext context, String CN, String DN) {
		SearchControls constraints = new SearchControls(); 
		constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		String attrList[] = {"cn","sAMAccountName","mailNickname","displayName","name","employeeID","title","telephoneNumber","company","co","department"};
		//String attrList[] = {"cn","sAMAccountName","displayName"};
		constraints.setReturningAttributes(attrList);
		String searchFilter = "(cn=" + CN + ")";
		String r="{\n";
		try {
			NamingEnumeration<SearchResult> answer = context.search(DN, searchFilter, constraints);
			SearchResult sr = answer.next();
			Attributes attrs = sr.getAttributes();
			if (attrs == null) {
				logger.debug("This result has no attributes");
			} else {
				for (NamingEnumeration<?> enumer = attrs.getAll(); enumer.hasMore();) {
					Attribute attrib = (Attribute) enumer.next();
					String element_name = "\""+attrib.getID().trim().replace("\n","") + "\"";
					try {
						for (NamingEnumeration<?> e = attrib.getAll(); e.hasMore(); ) {
							r+=element_name + ": \"" + e.next().toString().trim() + "\",\n";
							//System.out.println(element_name+ ":" + e.next());
						}
					} catch (Exception e){}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
			Calendar cal = Calendar.getInstance();
			r+="\"time\": \""+sdf.format(cal.getTime()) +"\",\n";
			r+="\"info\": \"Success\"\n";
			r+="}\n";
		} catch (Exception e) {
			r = GenerateError(e.getMessage());
		}
		return r;
	}
	
	private static String GenerateError(String err){
		return "{\"info\": \""+err+"\"}";
	}

	private static Map<String, Employee> get(String queryId, boolean includeDisabledAccount) throws NamingException {
		Map<String,Employee> map = new HashMap<>();
		String attr;
		DirContext ctx = GetContext(username, password);
		Map<String,String> DNMap = GetDN(ctx, queryId);
		for (String cn : DNMap.keySet()) {
			if(!StringUtils.isEmpty(DNMap.get(cn))) {
				logger.debug(DNMap.get(cn));
				if (!matchPerson(includeDisabledAccount, DNMap.get(cn))) {
					continue;
				}
				attr = GetAttributes(ctx, cn, DNMap.get(cn));
				logger.debug(attr);

				Employee employee = StringUtils.readJSON(attr, Employee.class);
				map.put(employee.getsAMAccountName(), employee);
			}
		}
		ctx.close();
		return map;
	}

	/**
	 * includeDisabledAccount = true
	 * usage: realtimeREST
	 */
	public static Map<String, Employee> get(List<String> collection) {
		Map<String,Employee> maps = new HashMap<>();
		for (String key : collection) {
			try {
				Map<String, Employee> result = get(key, true);
				maps.putAll(result);
			} catch (NamingException e) {
				logger.warn("not found user:" + key);
			}
		}
		return maps;
	}

	/**
	 * includeDisabledAccount = false
	 * usage: realtime report admin portal
	 */
	public static Map<String, LdapUserQuery> getFuzzyQuery(String queryId) throws NamingException {
		return getFuzzyQuery(queryId, false);
	}

	public static Map<String, LdapUserQuery> getFuzzyQuery(String queryId, boolean includeDisabledAccount) throws NamingException {
		Map<String, LdapUserQuery> map = new HashMap<>();
		LdapUserQuery userQuery;

		Map<String, Employee> employeeMap = get(queryId, includeDisabledAccount);
		for (String sAMAccountName : employeeMap.keySet()) {
			userQuery = new LdapUserQuery();
			//userQuery.setUserName(jsonData.get("cn").getAsString()); //2019.5.28 for ou=disabled accounts
			userQuery.setUserName(filterDisplayName(employeeMap.get(sAMAccountName).getDisplayName()));
			userQuery.setUserId(sAMAccountName);
			map.put(userQuery.getUserId(), userQuery);
		}
		return map;
	}

	private static String filterDisplayName(String displayName) {
		return displayName.substring(0, displayName.indexOf("(") - 1);
	}

	/**
	 * search userId and return userName from LDAP
	 * usage: ignitePersisStore
	 * @return userName (if not matching, result return original userId.)
	 */
    public static String getLdapUser(String userId, boolean includeDisabledAccount) {
        try {
            Map<String,LdapUserQuery> userMap = getFuzzyQuery(userId, includeDisabledAccount);
            for (LdapUserQuery userQuery : userMap.values()) {
                if (userQuery.getUserId().equals(userId)) {
                    return userQuery.getUserName();
                }
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }
        return userId;
    }

	public static void main(String args[]) {
		try {
			Map<String, LdapUserQuery> map = Ldap.getFuzzyQuery("ds37",true);
			map.entrySet().forEach(e -> System.out.println("k " + e.getKey() + ",\nv " + StringUtils.toGson(e.getValue())));
			System.out.println("getLdapUser:" + getLdapUser("ds37", false));
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
