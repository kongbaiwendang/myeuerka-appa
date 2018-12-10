package com.wyd.bootstrap.security.util;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.wyd.bootstrap.entity.constant.security.JwtDecodeResultType;

import net.minidev.json.JSONObject;

public class JwtUtil {
	private static final byte[] secret = "geiwodiangasfdjsikolkjikolkijswe".getBytes();

	/**
	 * 生成JWT的token
	 * 
	 * @param payloadMap
	 * @return
	 * @throws JOSEException
	 */
	public static String generateJwsToken(Map<String, Object> payloadMap) throws JOSEException {
		// 1、创建头部对象加密模式
		JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
		// 2、创建数据载体
		Payload payload = new Payload(new JSONObject(payloadMap));
		// 3、创建整体加密对象
		JWSObject jwsObject = new JWSObject(jwsHeader, payload);
		JWSSigner jwsSigner = new MACSigner(secret);
		// 签名
		jwsObject.sign(jwsSigner);
		return jwsObject.serialize();
	}

	/**
	 * 生成jws的超时时间等信息
	 * 
	 * @return
	 */
	public static Map<String, Object> generateJwsPayload() {
		Map<String, Object> payloadMap = new HashMap();
//		iss  【issuer】发布者的url地址
//		sub  【subject】该JWT所面向的用户，用于处理特定应用，不是常用的字段
//		aud  【audience】接受者的url地址
//		exp  【expiration】 该jwt销毁的时间；unix时间戳
//		nbf  【not before】 该jwt的使用时间不能早于该时间；unix时间戳
//		iat  【issued at】 该jwt的发布时间；unix 时间戳
//		jti  【JWT ID】 该jwt的唯一ID编号
		payloadMap.put("iat", System.currentTimeMillis());// 设置jwt生成时间
		payloadMap.put("nbf", System.currentTimeMillis());// 设置jwt生效时间
		payloadMap.put("exp", System.currentTimeMillis() + 600000);
		return payloadMap;
	}

	/**
	 * 验证并解析token是否有效
	 * @param token
	 * @return
	 * @throws ParseException
	 * @throws JOSEException
	 */
	public static Map<String, Object> valid(String token) throws ParseException, JOSEException {

		// 解析token
		JWSObject jwsObject = JWSObject.parse(token);
		// 获取到载体数据
		Payload payload = jwsObject.getPayload();
		// 建立一个解锁密匙
		JWSVerifier jwsVerifier = new MACVerifier(secret);

		Map<String, Object> resultMap = new HashMap<>();
		// 判断token
		if (jwsObject.verify(jwsVerifier)) {
			resultMap.put("Result", JwtDecodeResultType.SUCCESS.getType());
			// 载荷的数据解析成json对象。
			JSONObject jsonObject = payload.toJSONObject();
			resultMap.put("data", jsonObject);

			// 判断token是否过期
			if (jsonObject.containsKey("exp")) {
				Long expTime = Long.valueOf(jsonObject.get("exp").toString());
				Long nowTime = System.currentTimeMillis();
				// 判断是否过期
				if (nowTime.longValue() > expTime.longValue()) {
					// 已经过期
					resultMap.clear();
					resultMap.put("Result",  JwtDecodeResultType.TIMEOUT.getType());

				}
			}
		} else {
			resultMap.put("Result", JwtDecodeResultType.FAIL.getType());
		}
		return resultMap;

	}

}
