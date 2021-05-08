package com.pholema.tool.utils.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtils {

	// public static String md5(String key) {
	// if (key == null || key.isEmpty()) {
	// return "";
	// } else {
	// MessageDigest md;
	// try {
	// md = MessageDigest.getInstance("MD5");
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// return "";
	// }
	// md.update(key.toLowerCase().getBytes());
	// return new BigInteger(1, md.digest()).toString(16);
	// }
	// }

	public static String md5(String key) {
		if (key == null || key.isEmpty()) {
			return "";
		} else {
			return DigestUtils.md5Hex(key);
		}
	}

	// public static String SHA256(String key) {
	// if (key == null || key.isEmpty()) {
	// return "";
	// } else {
	// MessageDigest md;
	// try {
	// md = MessageDigest.getInstance("SHA-256");
	// } catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// return "";
	// }
	// md.update(key.toLowerCase().getBytes());
	// return new BigInteger(1, md.digest()).toString(16);
	// }
	// }
	public static String SHA256(String key) {
		if (key == null || key.isEmpty()) {
			return "";
		} else {
			return DigestUtils.sha256Hex(key);
		}
	}

	public static String hash(String key) {
		if (key == null || key.isEmpty()) {
			return "";
		} else {
			return Integer.toString(key.toLowerCase().hashCode());
		}
	}
}
