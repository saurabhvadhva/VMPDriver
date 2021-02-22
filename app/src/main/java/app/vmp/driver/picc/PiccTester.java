package app.vmp.driver.picc;

import android.os.Handler;
import android.os.Message;

import com.pax.dal.IPicc;
import com.pax.dal.entity.EDetectMode;
import com.pax.dal.entity.EM1KeyType;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.entity.PiccCardInfo;
import com.pax.dal.entity.PiccPara;
import com.pax.dal.exceptions.PiccDevException;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.vmp.driver.util.Convert;
import app.vmp.driver.util.GetObj;
import app.vmp.driver.util.TestLog;

import static com.pax.dal.entity.EPiccType.INTERNAL;

public class PiccTester extends TestLog {
    private static PiccTester piccTester;
    private IPicc picc;
    private static EPiccType piccType ;
    EPiccType type =INTERNAL;
    String nfccardnumber,CardAmount,CardHolderName,ReadNFCCARDID;
    private PiccTester(EPiccType type) {
        type=INTERNAL;
        piccType = type;

        picc = GetObj.getDal().getPicc(piccType);
        System.out.println(picc+"  type112  ");
    }

    public static PiccTester getInstance(EPiccType type) {
        if (piccTester == null || type != piccType) {
            piccTester = new PiccTester(type);
        }
        return piccTester;
    }
    public PiccPara setUp() {
        try {
            PiccPara readParam = picc.readParam();
            logTrue("readParam");
            return readParam;
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("readParam", e.toString());
            return null;
        }
    }

    public void open() {
        try {
            picc.open();
            logTrue("open");
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("open", e.toString());
        }
    }

    public PiccCardInfo detect(EDetectMode mode) {
        try {

            PiccCardInfo cardInfo = picc.detect(mode);
            logTrue("detect");
            return cardInfo;
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("detect", e.toString());
            return null;
        }
    }
    public void close() {
        try {
            picc.close();
            logTrue("close");
        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("close", e.toString());
        }
    }
    public void detectM(Handler handler, EM1KeyType type, int blockNum, String Namedata, byte[] password) {

        PiccCardInfo cardInfo = null;

        if (null != (cardInfo = detect(EDetectMode.ONLY_M))) {

            logTrue("cardtype:"
                    + cardInfo.getCardType()
                    + " SerialInfo:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getSerialInfo() == null) ? "".getBytes() : cardInfo.getSerialInfo())
                    + " cid:"
                    + cardInfo.getCID()
                    + " Other:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getOther() == null) ? "".getBytes() : cardInfo.getOther()));
            try {

                if(Namedata.length()<16) {
                    WriteCard(EM1KeyType.TYPE_A,  blockNum, password, cardInfo.getSerialInfo(),Namedata);
                }else{
                    System.out.print("Exceed the limit");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
        }

    }


    public void detectMIssueCard(Handler handler, EM1KeyType type, int blockNum, String Namedata, String cardnumber, String cardholdername, String NFCCardid, byte[] password) {

        PiccCardInfo cardInfo = null;

        if (null != (cardInfo = detect(EDetectMode.ONLY_M))) {

            logTrue("cardtype:"
                    + cardInfo.getCardType()
                    + " SerialInfo:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getSerialInfo() == null) ? "".getBytes() : cardInfo.getSerialInfo())
                    + " cid:"
                    + cardInfo.getCID()
                    + " Other:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getOther() == null) ? "".getBytes() : cardInfo.getOther()));
            try {
                if (cardholdername.length() > 16)
                    cardholdername = cardholdername.substring(0, 16) + "";

                // ----  store the Card Number in block number 16 ------//
                if(cardnumber.length()<16) {

                    WriteCard(EM1KeyType.TYPE_A,  16, password, cardInfo.getSerialInfo(),cardnumber);


                }else{
                    System.out.print("Exceed the limit");
                }


               // ----  store the card holder name in block number 17 ------//
                if(cardholdername.length()<16) {

                    WriteCard(EM1KeyType.TYPE_A,  17, password, cardInfo.getSerialInfo(),cardholdername);

                }else{
                    System.out.print("Exceed the limit");
                }

                // ----  store the Amount in block number 18 ------//

                if(Namedata.length()<16) {

                    WriteCard(EM1KeyType.TYPE_A,  blockNum, password, cardInfo.getSerialInfo(),Namedata);

                }else{
                    System.out.print("Exceed the limit");
                }


                // ----  store the current date  in block number 20 ------//

                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); // getting date in this format
                String currentdate = df.format(date.getTime());


                if(Namedata.length()<16) {

                    WriteCard(EM1KeyType.TYPE_A,  20, password, cardInfo.getSerialInfo(),currentdate);

                }else{
                    System.out.print("Exceed the limit");
                }


                // ----  store the current date  in block number 21 ------//

System.out.println("hhhh  "+NFCCardid);
                if(Namedata.length()<16) {

                    WriteCard(EM1KeyType.TYPE_A,  21, password, cardInfo.getSerialInfo(),NFCCardid);

                }else{
                    System.out.print("Exceed the limit");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
        }

    }

    public void detectMTopUpCard(Handler handler, EM1KeyType type, int blockNum, String Namedata, byte[] password) {

        PiccCardInfo cardInfo = null;

        if (null != (cardInfo = detect(EDetectMode.ONLY_M))) {

            logTrue("cardtype:"
                    + cardInfo.getCardType()
                    + " SerialInfo:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getSerialInfo() == null) ? "".getBytes() : cardInfo.getSerialInfo())
                    + " cid:"
                    + cardInfo.getCID()
                    + " Other:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getOther() == null) ? "".getBytes() : cardInfo.getOther()));
            try {

                // ----  store the Amount in block number 18 ------//

                if(Namedata.length()<16) {

                    WriteCard(EM1KeyType.TYPE_A,  blockNum, password, cardInfo.getSerialInfo(),Namedata);

                }else{
                    System.out.print("Exceed the limit");
                }
                // ----  store the current date  in block number 20 ------//

                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); // getting date in this format
                String currentdate = df.format(date.getTime());


                if(Namedata.length()<16) {

                    WriteCard(EM1KeyType.TYPE_A,  20, password, cardInfo.getSerialInfo(),currentdate);

                }else{
                    System.out.print("Exceed the limit");
                }


                String cardStringg = "";
                cardStringg += (nfccardnumber +","+CardAmount);

                Message message = Message.obtain();
                message.what = 0;
                message.obj = cardStringg;
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
        }

    }




    public void detectMTopUpnew(Handler handler, EM1KeyType type, int blockNum, String Namedata, byte[] password) {
        PiccCardInfo cardInfo = null;

        if (null != (cardInfo = detect(EDetectMode.ONLY_M))) {

            logTrue("cardtype:"
                    + cardInfo.getCardType()
                    + " SerialInfo:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getSerialInfo() == null) ? "".getBytes() : cardInfo.getSerialInfo())
                    + " cid:"
                    + cardInfo.getCID()
                    + " Other:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getOther() == null) ? "".getBytes() : cardInfo.getOther()));

            nfccardnumber= ReadCard(EM1KeyType.TYPE_A,  16, password, cardInfo.getSerialInfo());

            CardAmount= ReadCard(EM1KeyType.TYPE_A,  18, password, cardInfo.getSerialInfo());

            CardHolderName = ReadCard(EM1KeyType.TYPE_A,  17, password, cardInfo.getSerialInfo());

            ReadNFCCARDID = ReadCard(EM1KeyType.TYPE_A,  21, password, cardInfo.getSerialInfo());


            String cardString = "";
            cardString += (Convert.getInstance().bcdToStr(cardInfo.getSerialInfo())+" , "+nfccardnumber+" , "+CardAmount+" , "+CardHolderName+" , "+ReadNFCCARDID);

            Message message = Message.obtain();
            message.what = 0;
            message.obj = cardString;
            handler.sendMessage(message);

        } else {
            Message.obtain(handler, 0, "can't find card !").sendToTarget();
        }

    }



    public void detectMnew(Handler handler, EM1KeyType type, int blockNum, String Namedata, byte[] password) {
        PiccCardInfo cardInfo = null;

        if (null != (cardInfo = detect(EDetectMode.ONLY_M))) {

            logTrue("cardtype:"
                    + cardInfo.getCardType()
                    + " SerialInfo:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getSerialInfo() == null) ? "".getBytes() : cardInfo.getSerialInfo())
                    + " cid:"
                    + cardInfo.getCID()
                    + " Other:"
                    + Convert.getInstance().bcdToStr(
                    (cardInfo.getOther() == null) ? "".getBytes() : cardInfo.getOther()));


            String cardString = "";
            cardString += (Convert.getInstance().bcdToStr(cardInfo.getSerialInfo()));

            Message message = Message.obtain();
            message.what = 0;
            message.obj = cardString;
            handler.sendMessage(message);

        } else {
            Message.obtain(handler, 0, "can't find card !").sendToTarget();
        }

    }

    public static void memcpy(byte[] des, int start, String hexString, int len) {
        if (len != hexString.length()) {
            char[] achar = hexString.toLowerCase().toCharArray();
            for (int i = 0; i < len && i < (des.length - start) && (i * 2 + 1) < achar.length; i++) {
                int pos = i * 2;
                des[start + i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
            }
        } else {
            byte[] source = hexString.getBytes();
            for (int i = 0; i < len && i < (des.length - start) && i < source.length; i++)
                des[start + i] = source[i];
        }
    }

    public static int memcmp(byte[] buf1, int start1, byte[] buf2, int start2, int Asc_len) {
        for (int i = 0; i < Asc_len && i < (buf1.length - start1) && i < (buf2.length - start2); i++) {
            if (buf1[i + start1] < buf2[i + start2]) {
                return -1;
            } else if (buf1[i + start1] > buf2[i + start2]) {
                return 1;
            } else {
            }
        }
        return 0;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

    public String WriteCard(EM1KeyType typeA, int blocknumber, byte[] password, byte[] serialInfo, String Name){
        try{
            picc.m1Auth(typeA, (byte) blocknumber, password, serialInfo);

            StringBuffer sb = new StringBuffer();
            char ch[] = Name.toCharArray();
            byte[] bytewrite = new byte[16];
            for (int i = 0; i < ch.length; i++) {


                String hexastring = Integer.toHexString(ch[i]);
                bytewrite[i] = (byte) ((Character.digit(hexastring.charAt(0), 16) << 4) + Character.digit(hexastring.charAt(1), 16));
                sb.append(hexastring);
            }

            picc.m1Write((byte) blocknumber, bytewrite);

        }catch (Exception e){

        } return Name;
    }

    public String ReadCard(EM1KeyType typeA, int blocknumber, byte[] password, byte[] serialInfo){
        String backstring = "";
        try {
            picc.m1Auth(typeA, (byte) blocknumber, password, serialInfo);

            String str = "";
            String errStr = "";
            // byte[] value = m1Read((byte) blockNum);
            byte[] value = null;
            byte[] valuesecond = null;

            byte[] result = picc.m1Read((byte) blocknumber);
            if (result != null) {
                str = (Convert.getInstance().bcdToStr(result)+ "\n");
                System.out.print(str.length()+"  "+str);
                str=str.replace("00","");
                int length =str.length()%2!=0? str.length()-1 : str.length() ;

                valuesecond = new byte[length/2];
                //valuesecond = str.getBytes();
                int loppbyte = 0 ;
                int partitionSize=2;
                StringBuffer output = new StringBuffer(110);

                for (int i=0; i<length; i+=partitionSize)
                {
                    System.out.println(str.substring(i, Math.min(str.length(), i + partitionSize)));
                    if(str.substring(i, Math.min(str.length(), i + partitionSize)).equalsIgnoreCase("00")){

                        break;
                    }else {
                        valuesecond[i/2]=(byte)((Character.digit(str.charAt(i),16)<<4)+Character.digit(str.charAt(i+1),16));

                    }
                    loppbyte++;
                }



            } else {
                //     String errStr = "block " + blockNum + " read null";
                if (errStr == null || errStr.length() == 0)
                    str += "null";
                else
                    str += errStr;
                // logErr("m1Read", errStr);
            }




            logTrue("m1Read");
            System.out.print(new String(valuesecond) + "   gggggeeeee   ");
            backstring=new String(valuesecond);

        } catch (PiccDevException e) {
            e.printStackTrace();
            logErr("m1Read", e.toString());

        }


        return backstring;
    }

}
