package app.vmp.driver.util;

public class General {
	public static final int MAX_RSA_MODULUS_BITS = 2048;
	public static final int MAX_RSA_MODULUS_LEN = 256;
	public static final int MAX_RSA_PRIME_BITS = 1024;
	public static final int MAX_RSA_PRIME_LEN = 128;
	
	public static final int P60_S = 1;
	public static final int P70 = 2;
	public static final int P60_S1 = 3;
	public static final int P80 = 4;
	public static final int P78 = 5;
	public static final int P90 = 6;
	public static final int S80 = 7;
	public static final int SP30 = 8;
	public static final int S60 = 9;
	public static final int S90 = 10;
	public static final int S78 = 11;
	public static final int MT30 = 12;
	public static final int T52 = 13;
	public static final int S58 = 14;
	public static final int R50 = 0x80;
	public static final int P50 = 0x81;
	public static final int P58 = 0x82;
	public static final int R30 = 0x83;
	public static final int R50_M = 0x84;
	public static final int T80 = 0x85;
	public static final int T60 = 0x86;
	public static final int S980 = 17;
	public static final int A920 = 18;
	
	public static final int _POS_MT30_M = 16;
	public static final int _POS_SP30_M = 17;
	
	public static final int PN512 = 1;
	public static final int PN512_EGII = 2;
	public static final int RC663 = 3;
	
	//public static final int type = S980;
	public static final int ELE_NAME_MAX = 100;   /*The max length of every xml_element's  name*/
	public static final int XML_DOC_MAX = 8192;    /* The max length of the xml_document*/
	public static final int XML_INFO_MAX = 8192;    /* The max length of the xml_document*/
	public static final int XML_NAME_MAX = 100; 
	public static final String CAT_SETTING_FILE_NAME = "SETTING.INI";
	public static final String CAT_LOG_FILE_NAME = "CAT.LOG";

	public static final String CAT_STEP_LOG_FILE = "CAT_STEP.LOG";
	public static final String CAT_STEP_LOG_FILE_0 = "CAT_STEP.LOG.0";
	
	public static final byte COM1 = 0;
	public static final byte PINPAD = 3;
	public static final byte MODEM = 4;
	public static final byte USBDEV = 11;
	
	public static final byte CHARSET_WEST    =    0x01;     //������Ӣ������ŷ����        
	public static final byte CHARSET_TAI     =    0x02;      //̩��                        
	public static final byte CHARSET_MID_EUROPE = 0x03;      //��ŷ                           
	public static final byte CHARSET_VIETNAM   =  0x04;      //Խ��                           
	public static final byte CHARSET_GREEK    =   0x05;      //ϣ��                           
	public static final byte CHARSET_BALTIC   =   0x06;      //���޵ĺ�                       
	public static final byte CHARSET_TURKEY   =   0x07;      //������                         
	public static final byte CHARSET_HEBREW   =   0x08;      //ϣ����                          
	public static final byte CHARSET_RUSSIAN  =   0x09;     //����˹                        
	public static final byte CHARSET_GB2312  =    0x0A;      //��������      
	public static final byte CHARSET_GBK     =    0x0B;      //��������     
	public static final byte CHARSET_GB18030  =   0x0C;      //��������        
	public static final byte CHARSET_BIG5    =    0x0D;      //��������        
	public static final byte CHARSET_SHIFT_JIS  = 0x0E;      //�ձ�                          
	public static final byte CHARSET_KOREAN   =   0x0F;      //����                           
	public static final byte CHARSET_ARABIA  =    0x10;      //������	                          
	public static final byte CHARSET_DIY      =   0x11;      //�Զ�������
	
	
	
	public static void safememset(byte[] ptr, byte val)
    {
    	int i;
    	for(i=0;i<ptr.length;i++)ptr[i]=val;
    }
	public static byte[] memset(String hexString,int len)
    {
		int hexStringLen = hexString.length();
		if(hexStringLen == len)
		{
			byte[] buf = hexString.getBytes();
			return buf;
		}
		else
		{
			byte[] buf = new byte[len];
		    char[] achar = hexString.toLowerCase().toCharArray(); 
		    for (int i = 0; i < len; i++) { 
		     int pos = i * 2; 
		     buf[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1])); 
		    } 
		    return buf;
		}
    }
	
	/*public static void memset(byte[] ptr, byte val,int len)
    {
		Arrays.fill(ptr, 0, len, val);
    }
	public static void memset(char[] ptr, int val,int len)
    {
		Arrays.fill(ptr, 0, len, (char) val);
    }
	public static void memset(byte[] ptr,int start, byte val,int len)
    {
    	int i;
    	for(i=0;i<len;i++)ptr[i+start]=val;
    }
	public static void memset(int[] ptr, int val,int len)
    {
    	int i;
    	for(i=0;i<len;i++)ptr[i]=val;
    }*/
	public static void memcpy(byte[] des, byte[] source,int len)
	{
		memcpy(des,0,source,0,len);
	}
	public static void memcpy(byte[] des,int start, byte[] source,int len)
	{
		memcpy(des,start,source,0,len);
	}

	public static void memcpy(byte[] des, byte[] source,int start,int len)
	{
		memcpy(des,0,source,start,len);
	}
	public static void memcpy(byte[] des,int dstart, byte[] source,int sstart,int len)
	{
		int i;
    	for(i=0;(i<len)&&i<(des.length-dstart)&&i<(source.length-sstart);i++)
    		des[i+dstart]=source[i+sstart];
	}
	
	public static void memcpy(char[] des, char[] source,int len)
	{
		memcpy(des,0,source,0,len);
	}
	public static void memcpy(char[] des,int start, char[] source,int len)
	{
		memcpy(des,start,source,0,len);
	}

	public static void memcpy(char[] des, char[] source,int start,int len)
	{
		memcpy(des,0,source,start,len);
	}
	public static void memcpy(char[] des,int dstart, char[] source,int sstart,int len)
	{
		int i;
    	for(i=0;(i<len)&&i<(des.length-dstart)&&i<(source.length-sstart);i++)
    		des[i+dstart]=source[i+sstart];
	}
	
	public static void memcpy(char[] des, byte[] source,int len)
	{
		memcpy(des,0,source,0,len);
	}
	public static void memcpy(char[] des,int start, byte[] source,int len)
	{
		memcpy(des,start,source,0,len);
	}

	public static void memcpy(char[] des, byte[] source,int start,int len)
	{
		memcpy(des,0,source,start,len);
	}
	public static void memcpy(char[] des,int dstart, byte[] source,int sstart,int len)
	{
		int i;
    	for(i=0;(i<len)&&i<(des.length-dstart)&&i<(source.length-sstart);i++)
    		des[i+dstart]=(char) source[i+sstart];
	}


	public static void memcpy(char[] des, String source)
	{
		char[] sour = source.toCharArray();
		
		int i;
    	for(i=0;i<source.length()&&i<des.length;i++)des[i]=sour[i];
	}

	public static void memcpy(byte[] des, String hexString,int len)
	{
		memcpy(des,0,hexString,len);
	}
	public static void memcpy(byte[] des,int start, String hexString,int len)
	{
		if(len != hexString.length())
		{
			char[] achar = hexString.toLowerCase().toCharArray(); 
		    for (int i = 0; i < len&&i<(des.length-start)&&(i*2+1)<achar.length; i++) { 
			     int pos = i * 2; 
			     des[start+i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1])); 
			} 
		}
		else
		{
			byte[] source = hexString.getBytes();
			for(int i=0;i<len&&i<(des.length-start)&&i<source.length;i++)des[start+i]=source[i];
		}
	}
	private static byte toByte(char c) { 
	    byte b = (byte) "0123456789abcdef".indexOf(c); 
	    return b; 
	}

	public static void DatBcdToAsc(byte[] Asc, byte[] Bcd, int Asc_len)
	{
		DatBcdToAsc( Asc, 0, Bcd, 0, Asc_len);
	}
	public static void DatBcdToAsc(byte[] Asc, int aStart, byte[] Bcd,int Asc_len)
	{
		DatBcdToAsc( Asc, aStart, Bcd, 0, Asc_len);
	}
	public static void DatBcdToAsc(byte[] Asc, byte[] Bcd, int bStart, int Asc_len)
	{
		DatBcdToAsc( Asc, 0, Bcd, bStart, Asc_len);
	}
	public static void DatBcdToAsc(byte[] Asc, int aStart, byte[] Bcd, int bStart,int Asc_len)
	{
		/*~~~~~~~~~~~~~*/
		byte	is_first;
		byte	by;
		/*~~~~~~~~~~~~~*/

		is_first = (byte) (Asc_len % 2);				/* change by wxk 98.11.06 */

		int i = bStart;
		int j = aStart;
		while(Asc_len-- > 0)
		{
			if(is_first!=0)
			{
				by = (byte) (Bcd[i] & 0x0f);
				i++;
			}
			else
			{
				by = (byte) ((Bcd[i] >> 4) & 0x0f);
			}

			by += (by >= 0x0a) ? 0x37 : '0';	/* 0x37 = 'A' - 0x0a */

			Asc[j++] = by;
			if(is_first == 0)
			{
				is_first = 1;
			}
			else
			{
				is_first = 0;
			}
		}
	}
	
	public static void DatAscToBcd(byte[] Bcd, byte[] Asc, int Asc_len)
	{
		DatAscToBcd(Bcd, 0, Asc, 0,Asc_len);
	}
	public static void DatAscToBcd(byte[] Bcd,int bStart, byte[] Asc,int aStart, int Asc_len)
	{
		/*~~~~~~~~~~~~~~~~*/
		boolean	is_high; 
		byte by;
		/*~~~~~~~~~~~~~~~~*/

		is_high = (Asc_len % 2) == 0;
		Bcd[bStart] = 0x00;
		int i = bStart;
		int j = aStart;
		while(Asc_len-- > 0)
		{
			by = Asc[j++];

			if((by & 0x10) == 0 && (by > 0x30))
			{
				by += 9;
			}
			/* ����ĸ�Ϳո�Ĵ���,Сд���д,�ո��0 */
			if(is_high)
			{
				Bcd[i] = (byte) (by  << 4);
			}
			else
			{
				by &= 0x0f;
				Bcd[i++] |= by;
			}

			is_high = !is_high;
		}
	}
	public static int memcmp(byte[] buf1, byte[] buf2, int Asc_len)
	{
		return memcmp(buf1,0,buf2,0,Asc_len);
	}
	public static int memcmp(byte[] buf1,int start, byte[] buf2, int Asc_len)
	{
		return memcmp(buf1,start,buf2,0,Asc_len);
	}

	public static int memcmp(byte[] buf1, byte[] buf2,int start, int Asc_len)
	{
		return memcmp(buf1,0,buf2,start,Asc_len);
	}
	public static int memcmp(byte[] buf1,int start1, byte[] buf2,int start2, int Asc_len)
	{
		for(int i =0;i<Asc_len&&i<(buf1.length-start1)&&i<(buf2.length-start2);i++)
		{
			if(buf1[i+start1]<buf2[i+start2])
			{
				return -1;
			}
			else if(buf1[i+start1]>buf2[i+start2])
			{
				return 1;
			}
			else
			{
			}
		}
		return 0;
	}
	public static int memcmp(char[] buf1, char[] buf2, int Asc_len)
	{
		return memcmp(buf1,0,buf2,0,Asc_len);
	}
	public static int memcmp(char[] buf1,int start, char[] buf2, int Asc_len)
	{
		return memcmp(buf1,start,buf2,0,Asc_len);
	}

	public static int memcmp(char[] buf1, char[] buf2,int start, int Asc_len)
	{
		return memcmp(buf1,0,buf2,start,Asc_len);
	}
	public static int memcmp(char[] buf1,int start1, char[] buf2,int start2, int Asc_len)
	{
		for(int i =0;i<Asc_len&&i<(buf1.length-start1)&&i<(buf2.length-start2);i++)
		{
			if(buf1[i+start1]<buf2[i+start2])
			{
				return -1;
			}
			else if(buf1[i+start1]>buf2[i+start2])
			{
				return 1;
			}
			else
			{
			}
		}
		return 0;
	}
	public static int memcmp(String hexString, byte[] buf2, int Asc_len)
	{
		int hexStringLen = hexString.length();
		if(hexStringLen == Asc_len)
		{
			byte[] buf1 = hexString.getBytes();
			for(int i =0;i<Asc_len;i++)
			{
				if(buf1[i]<buf2[i])
				{
					return -1;
				}
				else if(buf1[i]>buf2[i])
				{
					return 1;
				}
			}
		}
		else
		{
		    char[] achar = hexString.toLowerCase().toCharArray(); 
		    byte[] buf1 = new byte[Asc_len];
		    for (int i = 0; i < Asc_len; i++) { 
		     int pos = i * 2; 
		     buf1[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1])); 
		    }
		    for(int i =0;i<Asc_len;i++)
			{
				if(buf1[i]<buf2[i])
				{
					return -1;
				}
				else if(buf1[i]>buf2[i])
				{
					return 1;
				}
			}
		}
		return 0;
	}
	public static int memcmp(byte[] buf1, String hexString, int Asc_len)
	{
		return memcmp(buf1,0,hexString,Asc_len);
	}
	public static int memcmp(byte[] buf1,int start, String hexString, int Asc_len)
	{
		int hexStringLen = hexString.length();
		if(hexStringLen == Asc_len)
		{
			byte[] buf2 = hexString.getBytes();
			for(int i =0;i<Asc_len&&i<(buf1.length-start)&&i<hexStringLen;i++)
			{
				if(buf1[i+start]<buf2[i])
				{
					return -1;
				}
				else if(buf1[i+start]>buf2[i])
				{
					return 1;
				}
			}
		}
		else
		{
		    char[] achar = hexString.toLowerCase().toCharArray(); 
		    byte[] buf2 = new byte[Asc_len];
		    for (int i = 0; i < Asc_len&&i<(buf1.length-start)&&(i*2+1)<hexStringLen; i++) { 
		    	int pos = i * 2; 
		    	buf2[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1])); 
		    }
		    for(int i =0;i<Asc_len&&i<(buf1.length-start);i++)
			{
				if(buf1[i+start]<buf2[i])
				{
					return -1;
				}
				else if(buf1[i+start]>buf2[i])
				{
					return 1;
				}
			}
		}
		return 0;
	}
	public static int strcmp(byte[] buf1, String str)
	{
		byte[] buf2 = str.getBytes();
		return strcmp(buf1, buf2);
	}
	public static int strcmp(char[] buf1, String str)
	{
		char[] buf2 = str.toCharArray();
		for(int i =0;i<buf1.length&&i<buf2.length;i++)
		{
			if(buf1[i]<buf2[i])
			{
				return -1;
			}
			else if(buf1[i]>buf2[i])
			{
				return 1;
			}
		}
		return 0;
	}
	public static int strcmp(byte[] buf1, byte[] buf2)
	{
		for(int i =0;i<buf1.length&&i<buf2.length;i++)
		{
			if(buf1[i]<buf2[i])
			{
				return -1;
			}
			else if(buf1[i]>buf2[i])
			{
				return 1;
			}
		}
		return 0;
	}
	public static void strcpy(byte[] des, byte[] source,int start)
	{
		for(int i =0;i<source.length&&(i+start)<des.length;i++)
		{
			des[i+start] = source[i];
		}
	}
	public static void strcpy(char[] des,int start, byte[] source)
	{
		for(int i =0;i<source.length&&(i+start)<des.length;i++)
		{
			des[i+start] = (char) source[i];
		}
	}
	public static void strcpy(byte[] des, byte[] source)
	{
		for(int i =0;i<source.length&&i<des.length;i++)
		{
			des[i] = source[i];
		}
	}
	public static void strcpy(byte[] des, String source)
	{
		byte[] temp = source.getBytes();
		for(int i =0;i<temp.length&&i<des.length;i++)
		{
			des[i] = temp[i];
		}
	}
	public static void strcpy(char[] des, String source)
	{
		for(int i =0;i<des.length&&i<source.length();i++)
		{
			des[i] = source.charAt(i);
		}
	}
	public static void strcpy(char[] des,int start, String source)
	{
		for(int i =0;i<source.length()&&(i+start)<des.length;i++)
		{
			des[i+start] = source.charAt(i);
		}
	}
	public static void strcpy(char[] des, char[] source)
	{
		strcpy(des,0,source,0);
	}
	public static void strcpy(char[] des, int dStart, char[] source,int sStart)
	{
		for(int i =0;(i+sStart)<source.length&&(i+dStart)<des.length;i++)
		{
			des[i+dStart] = source[i+sStart];
		}
	}
	public static int sprintf(char[] des, String source)
	{
		char temp[] = source.toCharArray();
		for(int i =0;i<temp.length&&i<des.length;i++)
		{
			des[i] = temp[i];
		}
		return source.length();
	}
	public static int  sprintf(byte[] des, String source)
	{
		byte temp[] = source.getBytes();
		for(int i =0;i<temp.length&&i<des.length;i++)
		{
			des[i] = temp[i];
		}
		return source.length();
	}
	public static int  sprintfEndWithZero(byte[] des, String source)
	{
		byte temp[] = source.getBytes();
		int i;
		for(i =0;i<temp.length&&i<des.length;i++)
		{
			des[i] = temp[i];
		}
		if(source.charAt(source.length()-1)==0x00)
		{
			return source.length();
		}
		else
		{
			des[i] = 0x00;
			return source.length()+1;
		}
		
	}

}
