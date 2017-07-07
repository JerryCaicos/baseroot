package com.base.application.baseapplication.utils;

import android.text.TextUtils;

import com.base.application.baseapplication.BasicApplication;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adminchen on 16/1/16.
 */
public class StringUtils
{
	/**
	 * @Description: 转半角的函数
	 * @Date 2013-6-14
	 * <p>
	 * <pre>
	 *          全角空格为12288
	 *          半角空格为32
	 * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 *
	 *       <pre>
	 */
	public static String convertToDBC(String input)
	{
		char[] c = input.toCharArray();
		int len = c.length;
		for(int i = 0;i < len;i++)
		{
			if(c[i] == 12288)
			{
				c[i] = (char)32;
				continue;
			}
			if(c[i] > 65280 && c[i] < 65375)
			{
				c[i] = (char)(c[i] - 65248);
			}
		}
		return new String(c);
	}

	/**
	 * @Description: java科学计数法转换成普通计数法
	 * @Date 2013-6-27
	 */
	public static String convertToNormal(String str)
	{
		BigDecimal db = new BigDecimal(str);
		return db.toPlainString();
	}

	/**
	 * @Description: 删掉字符串中所以特殊可见字符
	 * @Date 2013-6-14
	 */
	public static String delAllSpecialChar(String keyword)
	{
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(keyword);

		return m.replaceAll("").trim();
	}

	/**
	 * @Description: 删掉字符串中的不可见字符
	 * @Author 13050527
	 * @Date 2013-6-14
	 */
	public static String delBlank(String str)
	{
		Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll("");
	}

	/**
	 * @Description: 如果输入为12.010000，返回 12.01 ;如果输入为12，返回12.00;如果输入为0,返回0.00
	 * @param:
	 */
	public static String formatPrice(String price)
	{
		String tempPrice = "0.00";
		if(!TextUtils.isEmpty(price))
		{
			DecimalFormat df = new DecimalFormat("##0.00");
			tempPrice = df.format(Double.parseDouble(price));
		}
		return tempPrice;
	}

	/**
	 * @Description: 將中文中的一些特殊字符转为英文中的字符
	 * @Date 2013-6-14
	 */
	public static String filterSpecChineseChar(String str)
	{
		str = str.replaceAll("【","[").replaceAll("】","]")
				 .replaceAll("！","!");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * @Description: 还原11位手机号,去除“-”" ""+86""86"字符
	 * @Date 2014-9-15
	 */
	public static String filterPurePhoneNumber(String input)
	{
		String num;
		if(null != input)
		{
			num = input.replaceAll("-","");
			num = num.replace(" ","");
			if(num.startsWith("+86"))
			{
				num = num.substring(3);
			}
			else if(num.startsWith("86"))
			{
				num = num.substring(2);
			}
		}
		else
		{
			num = "";
		}
		return num;
	}

	/**
	 * @param content
	 * @return
	 * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
	 */
	public static int getCharacterNum(final String content)
	{
		if(null == content || "".equals(content))
		{
			return 0;
		}
		else
		{
			return (content.length() + getChineseNum(content));
		}
	}

	/**
	 * @param s
	 * @return
	 * @description 返回字符串里中文字或者全角字符的个数
	 */
	public static int getChineseNum(String s)
	{
		int num = 0;
		char[] myChar = s.toCharArray();
		for(int i = 0;i < myChar.length;i++)
		{
			if((char)(byte)myChar[i] != myChar[i])
			{
				num++;
			}
		}
		return num;
	}

	/**
	 * @Description: 字符串类型转化成int类型
	 */
	public static int getIntFromStr(String str)
	{
		int i = 0;
		try
		{
			i = Integer.valueOf(str);
		}
		catch(NumberFormatException e)
		{
		}
		return i;
	}

	/**
	 * @Description: String类型转换为Double
	 */
	public static Double getDoubleFromStr(String str)
	{
		Double i = 0.0;
		try
		{
			i = Double.valueOf(str);
		}
		catch(Exception e)
		{
		}
		return i;
	}

	/**
	 * 判断字符中是否包含特殊字符
	 *
	 * @param str
	 */
	public static boolean hasSpecialChar(String str)
	{
		boolean isSpecial = false;
		char[] addressChar = str.toCharArray();
		int s = addressChar.length;
		Pattern pAddress = Pattern
				.compile("[`~!$%^&*+=|{}':;',\"\\[\\].<>/?~！￥%……&*——+|{}【】‘；：”“’。，、？\\\\]");
		for(int i = 0;i < s;i++)
		{
			if(pAddress.matcher(String.valueOf(addressChar[i])).matches())
			{
				isSpecial = true;
			}
		}
		return isSpecial;
	}

	/**
	 * 判断给定字符串是否空白串。<br>
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 若输入字符串为null或空字符串，返回true
	 *
	 * @param input
	 * @return boolean
	 */
	public static boolean isBlank(String input)
	{
		if(input == null || "".equals(input))

		{
			return true;
		}

		int len = input.length();
		for(int i = 0;i < len;i++)
		{
			char c = input.charAt(i);
			if(c != ' ' && c != '\t' && c != '\r' && c != '\n')
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 判定输入汉字
	 *
	 * @param c
	 * @return
	 */
	public static boolean isChineseChar(char c)
	{
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
		{
			return true;
		}
		return false;
	}

	/**
	 * @Description: 是否为纯数字
	 */
	public static boolean isAllNum(String str)
	{
		Pattern patternNum = Pattern.compile("^\\d+$");// 是否为纯数字
		if(patternNum.matcher(str).matches())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 判断是否含有双字节
	 */
	public static boolean isDoubleCharacter(String c)
	{
		String regexp = "[^\\x00-\\xff]";// 双字节字符
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(c);
		if(m.find())
		{
			return true;
		}
		return false;
	}

	/**
	 * @Description: 匹配邮箱格式
	 */
	public static boolean isEmail(String str)
	{
		String regex = "^[A-Za-z0-9]+([._\\-]*[A-Za-z0-9])*@([A-Za-z0-9]+[-A-Za-z0-9]*[A-Za-z0-9]+.){1,63}[A-Za-z0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 检查一个string是否是十六进制
	 */
	public static boolean isHexNumber(String str)
	{
		try
		{
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	/**
	 * @param input
	 * @return true ：手机号码格式错误
	 * <p>
	 * false: 手机号码格式正确
	 * </p>
	 * @Description 验证手机号码 是否为 以1开头的11位数字
	 */
	public static boolean isPhone(String input)
	{
		if(TextUtils.isEmpty(input))
		{
			return false;
		}
		Pattern pat = Pattern.compile("1\\d{10}");
		return pat.matcher(input).matches();
	}

	/**
	 * @Title: 校验密码格式是否符合规则
	 * @Description: 密码必须由纯数字、纯字母、特殊符号（-\\da-zA-Z`=\"\\[\\],./~!@#$%^&*()_+|:]+）
	 * 这三类最少两类的组合才符合条件 例如："123456789"、“abcdefg”都是错误的密码；
	 * "123456a"、"abcdefg123"、"123456#"、"1234ab$"等都是正确的密码
	 */
	public static boolean isPwdMatchRule(String str)
	{
		int num = 0;
		num = Pattern.compile("\\d").matcher(str).find()?num + 1:num;
		num = Pattern.compile("[a-zA-Z]").matcher(str).find()?num + 1:num;
		char[] specialChar = str.toCharArray();
		int s = specialChar.length;
		boolean hasUnFitSpecialChar = false;
		Pattern pAddress = Pattern
				.compile("[-\\da-zA-Z`=\"\\[\\],./~!@#$%^&*()_+|:]+");
		for(int i = 0;i < s;i++)
		{
			if(!pAddress.matcher(String.valueOf(specialChar[i])).find())
			{
				hasUnFitSpecialChar = true;
			}
		}
		num = Pattern.compile("[-`=\"\\[\\],./~!@#$%^&*()_+|:]+").matcher(str)
					 .find()?num + 1:num;
		if(!hasUnFitSpecialChar)
		{
			return num >= 2;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 检测String是否包含中文
	 */
	public static boolean isStrObtainChinese(String str)
	{
		char[] ch = str.toCharArray();
		for(int i = 0;i < ch.length;i++)
		{
			char c = ch[i];
			if(isChineseChar(c))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测String是否全是中文
	 *
	 * @param name
	 * @return
	 */
	public static boolean isStrAllChinese(String name)
	{
		boolean res = true;
		char[] cTemp = name.toCharArray();
		for(int i = 0;i < name.length();i++)
		{
			if(!isChineseChar(cTemp[i]))
			{
				res = false;
				break;
			}
		}
		return res;
	}

	/**
	 * @param resId 资源ID
	 * @return String
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-10-29
	 */
	public static String getString(int resId)
	{
		return BasicApplication.getInstance().getResources().getString(resId);
	}

	/**
	 * @param resId      资源ID
	 * @param formatArgs 插入的内容
	 * @return String
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-10-29
	 */
	public static String getString(int resId,Object... formatArgs)
	{
		return BasicApplication.getInstance().getResources().getString(resId,formatArgs);
	}

	/**
	 * @param resId 资源ID
	 * @return String
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-10-29
	 */
	public static String[] getStringArray(int resId)
	{
		return BasicApplication.getInstance().getResources().getStringArray(resId);
	}
}
