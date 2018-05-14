package com.itheima.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

	
	/**��ָ���ַ�������md5�㷨ȥ����
	 * @param psd��Ҫ���ܵ�����
	 * @return ���ܺ��32λ�ַ���
	 */
	public static String encoder(String psd) {
		try {
			//1,ָ�������㷨����
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//2,����Ҫ���ܵ��ַ�����ת����byte���͵�����,Ȼ����������ϣ����
			byte[] bs = digest.digest(psd.getBytes());
//			System.out.println(bs.length);
			//3,ѭ������bs,Ȼ����������32λ�ַ���,�̶�д��
			//4,ƴ���ַ�������
			StringBuffer stringBuffer = new StringBuffer();
			for (byte b : bs) {
				int i = b & 0xff;
				//int���͵�i��Ҫת����16�����ַ�
				String hexString = Integer.toHexString(i);
//				System.out.println(hexString);
				if(hexString.length()<2){
					hexString = "0"+hexString;
				}
				stringBuffer.append(hexString);
			}
			//5,��ӡ����
			System.out.println(stringBuffer.toString());
			
			return stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}
		return " ";
		
	}
}
