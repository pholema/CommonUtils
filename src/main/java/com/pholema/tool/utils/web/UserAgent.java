package com.pholema.tool.utils.web;



public class UserAgent {
    public static  void main( String[] args )
    {
    	/*
    	 * PaleMoon
    	 * PaleMoon, version=27.5.0
    	 * Toggle function doesn't work
    	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:52.9) Gecko/20100101 Goanna/3.3 Firefox/52.9 PaleMoon/27.5.0
    	 * 
    	 * Firefox
    	 * Firefox, version=47.0
    	 * Toggle function doesn't work 
    	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0
    	 * 
    	 * Netscape
    	 * Navigator, version=9.0.0.6
    	 * Mozilla/5.0 (Windows; U; Windows NT 6.2; en-US; rv:1.8.1.12) Gecko/20080219 Firefox/2.0.0.12 Navigator/9.0.0.6
    	 * 
    	 * Internet Explorer
    	 * UserAgentBrowser [browser=IE, version=7.0, os=Windows]
    	 * Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; Tablet PC 2.0; InfoPath.3; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR
    	 * 
    	 * Edge
    	 * No
    	 * UserAgentBrowser [browser=Chrome, version=46.0.2486.0, os=Windows]
    	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586
    	 *
    	 * brave
    	 * Chrome, version=61.0.3163.79
    	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36
    	 * */
    	UserAgentBrowser b= com.pholema.tool.utils.web.UserAgent.GetUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36");
    	
    	System.out.println(b.toString());
    	b=null;
    	
    }
    
    public static UserAgentBrowser GetUserAgent(String userAgent) {
    	String user=userAgent.toLowerCase();
    	UserAgentBrowser rst=new UserAgentBrowser();
    	rst.os=getOS(userAgent);
        if (user.contains("msie")){
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            rst.browser=substring.split(" ")[0].replace("MSIE", "IE");
            rst.version=substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version")){
        	rst.browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0];
        	rst.version=(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera")){
            if(user.contains("opera")){
            	rst.browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0];
            	rst.version=(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
            	String[] ar=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera").split("-");
            	rst.browser=ar[0];
            	if(ar.length>1) rst.version=ar[1];
            }
        } else if (user.contains("chrome")){
        	String ar[]=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-").split("-");
        	rst.browser=ar[0];
        	if(ar.length>1) rst.version=ar[1];
        } else if ((user.indexOf("navigator") > -1) || (user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) ){
        	String ar[]=(userAgent.substring(userAgent.indexOf("Navigator")).split(" ")[0]).replace("/", "-").split("-");
        	rst.browser = ar[0];
        	if(ar.length>1) rst.version=ar[1];
        } else if(user.contains("palemoon")){
        	String[] ar=(userAgent.substring(userAgent.indexOf("PaleMoon")).split(" ")[0]).replace("/", "-").split("-");
        	rst.browser=ar[0];
        	if(ar.length>1) rst.version=ar[1];
        }else if (user.contains("firefox")){
        	String[] ar=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-").split("-");
        	rst.browser=ar[0];
        	if(ar.length>1) rst.version=ar[1];
        } else if(user.contains("rv")){
        	rst.browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else{
        	rst.browser = "UnKnown, More-Info: "+userAgent;
        }
    	return rst;
    }
    
    public static String getBrowser(String userAgent){
    	String user=userAgent.toLowerCase();
    	String browser="";
        if (user.contains("msie")){
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        return browser;
    }
    
    public static String getOS(String source){
    	String os="";
    	String userAgent=source.toLowerCase();
        if (userAgent.indexOf("windows") >= 0 ){
       	 	os = "Windows";
        } else if(userAgent.indexOf("mac") >= 0){
       	 	os = "Mac";
        } else if(userAgent.indexOf("x11") >= 0){
       	 	os = "Unix";
        } else if(userAgent.indexOf("android") >= 0){
       	 	os = "Android";
        } else if(userAgent.indexOf("iphone") >= 0){
       	 	os = "IPhone";
        }else{
       	 	os = "UnKnown, More-Info: "+userAgent;
        }
        return os;
    }
}



