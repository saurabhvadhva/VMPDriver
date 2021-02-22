package app.vmp.driver.picc;

import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class SeCard {
    public static void activate(NfcA tag) throws Exception {
            byte[] rersp = tag.transceive(new byte[]{(byte)0xE0, (byte)0x80});
            feedback((new byte[]{(byte)0xE0, (byte)0x80}),rersp);
    }


    public static void Switch2SLE1(NfcA typeA, String aes_key) throws Exception {
            byte [] resp = typeA.transceive(ALEUtil.hex2byte("A80090"+aes_key));
            feedback(ALEUtil.hex2byte("A80090"),resp); resp = null;
            resp = typeA.transceive(ALEUtil.hex2byte("A80190"+aes_key));
            feedback(ALEUtil.hex2byte("A80190"),resp); resp = null;
            resp = typeA.transceive(ALEUtil.hex2byte("A80390"+aes_key));
            feedback(ALEUtil.hex2byte("A80390"),resp); resp = null;
            resp = typeA.transceive(ALEUtil.hex2byte("A80490"+aes_key));
            feedback(ALEUtil.hex2byte("A80490"),resp); resp = null;
            resp = typeA.transceive(new byte[]{(byte)0xAA});
            feedback(ALEUtil.hex2byte("AA"),resp); resp = null;
    }



    public static String ReadWithA(MifareClassic classic, String key, int sec, int blk)
            throws Exception {
        //FF D6 0003 10 001122334455 787788 69 66778899AABB
            classic.authenticateSectorWithKeyA(sec, ALEUtil.hex2byte(key));
            byte[] resp = classic.readBlock(blk); //.writeBlock(7, ALEUtil.hex2byte(data));
            //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
            Log.d("PLUS-WRITE", "-- Read Success --" + ALEUtil.hexString(resp));
            return ALEUtil.hexString(resp);
    }

    public static void WriteWithB(MifareClassic classic, String data, String keyB, int sec, int blk)
            throws Exception
    {
        //FF D6 0003 10 001122334455 787788 69 66778899AABB
        classic.authenticateSectorWithKeyB(sec, ALEUtil.hex2byte(keyB));
        classic.writeBlock(blk, ALEUtil.hex2byte(data));
        //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
        Log.d("PLUS-WRITE", "------------- Write Success -------------");
    }

    public static String ReadData(MifareClassic classic, String key, int sec, int blk)
            throws Exception {
        //FF D6 0003 10 001122334455 787788 69 66778899AABB
        classic.authenticateSectorWithKeyA(sec,ALEUtil.hex2byte(key));
        byte[] resp = classic.readBlock(blk); //.writeBlock(7, ALEUtil.hex2byte(data));
        //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
        Log.d("PLUS-WRITE", "-- Read Success --" + ALEUtil.hexString(resp));
        return ALEUtil.hexString(resp);
    }


    public static String ReadValue(MifareClassic classic)
            throws Exception {
        //FF D6 0003 10 001122334455 787788 69 66778899AABB
        int sector = classic.blockToSector(5);
        classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
        byte[] resp = classic.readBlock(5); //.writeBlock(7, ALEUtil.hex2byte(data));
        //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
        Log.d("PLUS-WRITE", "-- Read value Success --" + ALEUtil.hexString(resp));
        return ALEUtil.hexString(resp);
    }

    public static void WriteData(MifareClassic classic, String data, String keyB, int sec, int blk)
    throws Exception
    {
        //FF D6 0003 10 001122334455 787788 69 66778899AABB
        classic.authenticateSectorWithKeyA(sec, ALEUtil.hex2byte(keyB));
        classic.writeBlock(blk, ALEUtil.hex2byte(data));
        //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
        Log.d("PLUS-WRITE", "------------- Write Success -------------");
    }


    public static void CreateWallet(MifareClassic classic) throws Exception {
        int sector = 0;
        int default_val = 1;
        int inc_val = 5;
        int blk = classic.sectorToBlock(sector);
        blk = blk+2;
        classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
        Log.d("LOGIC-AUTH", "------------- Auth Success -------------");
        byte[] default_value_block = valueblock(default_val,blk);
        Log.d("LOGIC-val_block", ALEUtil.hexString(default_value_block));
        classic.writeBlock(blk,default_value_block);
        Log.d("LOGIC-write_block", "------------- Write Success -------------");

    }

    public static void CreateWalletLive(MifareClassic classic, int sector) throws Exception {
        //int blk = 5;
        int default_val = 1;
        int inc_val = 5;
        //int sector = classic.blockToSector(blk);
        int blk = classic.sectorToBlock(sector);
        // create wallet on second block
        blk = blk+2;
        classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
        Log.d("LOGIC-AUTH", "------------- Auth Success -------------");
        byte[] default_value_block = valueblock(default_val,blk);
        Log.d("LOGIC-val_block", ALEUtil.hexString(default_value_block));
        classic.writeBlock(blk,default_value_block);
        Log.d("LOGIC-write_block", "------------- Write Success -------------");

    }


    /*
        0) Value: Signifies a signed 4-byte value. The lowest significant byte of a value is stored
        in the lowest address byte. Negative values are stored in standard 2´s complement
        format. For reasons of data integrity and security, a value is stored three times, twice
        non-inverted and once inverted.
        0) Adr: Signifies a 1-byte address, which can be used to save the storage address of a
        block, when implementing a powerful backup management. The address byte is stored
        four times, twice inverted and non-inverted. During increment, decrement, restore and
        transfer operations the address remains unchanged. It can only be altered via a write
        command.
     */
    public static void Credit(MifareClassic classic, int val)
    throws Exception {
            int sector = 0;
            int blk = classic.sectorToBlock(sector);
            blk = blk+2;
            classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
            classic.increment(blk,val);
            classic.transfer(blk);
            // bug in android, following line fixes it
            byte[] updates_value = classic.readBlock(blk);
            //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
            Log.d("PLUS-CREDIT", "------------- Credit Success -------------");
    }

    public static void CreditB(MifareClassic classic, String keyB, int blk, int val)
            throws Exception {
        int sector = classic.blockToSector(blk);
        boolean status = classic.authenticateSectorWithKeyB(sector, ALEUtil.hex2byte(keyB));
        if (status) {
            classic.increment(blk, val);
            classic.transfer(blk);
        } else {
            throw new Exception("Auth Error");
        }
        // bug in android, following line fixes it
        byte[] updates_value = classic.readBlock(blk);
        //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
        Log.d("PLUS-CREDIT", "------------- Credit Success -------------");
        int amt = getAmtFromBlock(updates_value);
        Log.d("LOGIC-balance", "------------- " + amt +" -------------");

    }


    public static void cardLogic(MifareClassic classic) throws IOException {
        int blk = 6;
        int default_val = 100;
        int inc_val = 5;
        int sector = classic.blockToSector(blk);
        classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
        Log.d("LOGIC-AUTH", "------------- Auth Success -------------");
//        byte[] default_value_block = valueblock(default_val,blk);
//        Log.d("LOGIC-val_block", ALEUtil.hexString(default_value_block));
//        classic.writeBlock(blk,default_value_block);
//        Log.d("LOGIC-write_block", "------------- Write Success -------------");
        classic.increment(blk,inc_val);
        Log.d("LOGIC-write_block", "------------- Increment Success -------------");
        classic.transfer(blk);
        Log.d("LOGIC-write_block", "------------- Transfer Success -------------");
        byte[] updates_value = classic.readBlock(blk);
        Log.d("LOGIC-updated_value", ALEUtil.hexString(updates_value));
        int amt = getAmtFromBlock(updates_value);
        Log.d("LOGIC-balance", "------------- " + amt +" -------------");

    }

    public static int Balance(MifareClassic classic) throws Exception {
        int default_val = 100;
        int inc_val = 5;
        //int sector = classic.blockToSector(blk);
        int sector = 0;
        int blk = classic.sectorToBlock(sector);
        blk = blk+2;
        classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
        Log.d("LOGIC-AUTH", "------------- Auth Success -------------");
        byte[] updated_value = classic.readBlock(blk);
        Log.d("LOGIC-updated_value", ALEUtil.hexString(updated_value));
        int amt = getAmtFromBlock(updated_value);
        Log.d("LOGIC-balance", "------------- " + amt +" -------------");
        return amt;
    }


    public static void Debit(MifareClassic classic, int val) throws Exception {
        int sector = 0;
        int blk = classic.sectorToBlock(sector);
        blk = blk+2;
        classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
        classic.decrement(blk,val);
        classic.transfer(blk);
        byte[] updates_value = classic.readBlock(blk);
        //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
        Log.d("PLUS-DEBIT", "------------- Debit Success -------------");
    }

    public static void DebitA(MifareClassic classic, String keyA, int blk, int val) throws Exception {
        int sector = classic.blockToSector(blk);
        boolean status = classic.authenticateSectorWithKeyA(sector, ALEUtil.hex2byte(keyA));
        if (status) {
            classic.decrement(blk, val);
            classic.transfer(blk);
        } else {
            throw new Exception("Auth Error");
        }
        byte[] updates_value = classic.readBlock(blk);
        //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
        Log.d("PLUS-DEBIT", "------------- Debit Success -------------");
        int amt = getAmtFromBlock(updates_value);
        Log.d("LOGIC-balance", "------------- " + amt +" -------------");

    }

    /*
                    -------------------- Secret sauce --------------------------
    Have a look to table 7 and 8 (I refer to the MIFARE Classic data sheet with the link above).
    You can see in table 7, with none combination of C1, C2 and C3 you can read the key A,
    so key A is a write-only value. C1 = 0, C2 = 0 and C3 = 1 is the so called “transport condition”
    for the sector trailer. With the known value of default key A = FF…FF you authenticate with
    key A to the sector and you are able to read the access condition (ac) bytes, the key B.
    You are also able to write to key A, the ac bytes, key B. Table 8 shows the ac bytes for
    the data blocks 0, 1 and 2. Here is the transport condition C1 = C2 = C3 = 0. This means,
    you can read, write, increment, decrement all data blocks either with key A or key B.

    You want to use key A for reading and key B for writing. This is the setting for the
    data blocks. In this case key A is known at the reader terminal. Let us assume you want
    to allow reading data block 0 (the customer ID) and decrement a credit (data value in block 1).
    But with key A it should not be possible to write to the ID data nor to increment the credit
    value. In this case C1 = 1, C2 = 1 and C3 = 0 is the ac combination for the data blocks
    (table 8). Writing end-user data or set a new value for the credit is only possible at the
    issuer station (with a cashbox) and only here the key B is known.

    The sector trailer ac combination could be C1 = 0, C2 = 1, C3 =1 (table 7). Key A and key B
    can never be read but can be written with key B. Okay, we have:
    C10 = 1, C20 = 1, C30 = 0,
    C11 = 1, C21 = 1, C31 = 0,
    C12 = 1, C22 = 1, C32 = 0,
    C13 = 0, C23 = 1, C33 = 1

    With this information we go into figure 10 (page 12) and we get for bytes 6, 7 and 8:

    00001000  01110111  10001111 = 08778F

     */
    public static void ChangeKeys(MifareClassic classic,
                                  String keyA,
                                  String keyB, int sector) throws Exception {
        //FF D6 0003 10 001122334455 787788 69 66778899AABB
        String secret_sauce = "08778F00";
//        try {
            //int sector = classic.blockToSector(blk);
            int blk = classic.sectorToBlock(sector);
            blk = blk+3;
            String record = keyA+secret_sauce+keyB;
            classic.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
            classic.writeBlock(blk, ALEUtil.hex2byte(record));
            //feedback(new byte[]{(byte)0xFF, (byte)0xD6},resp); resp = null;
            Log.d("PLUS-WRITE", "-------------Change Key Success -------------");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("TRCE-ERROR", e.getLocalizedMessage());
//        }
    }


    /****************************************************************************************************\
     *              All private functions
     * **************************************************************************************************/

    // feedback/debug: a request-response round
    private static void feedback(byte[] command, byte[] response) {
        out(command);
        in(response);
    }

    // feedback/debug: PCD --> PICC
    private static void out(byte[] command) {
        Log.i("NFC-TRACE",">> " + Dump.hex(command, true));
    }

    // feedback/debug: PICC --> PCD
    private static void in(byte[] response) {
        Log.i("NFC-TRACE","<< " + Dump.hex(response, true));
    }

    static byte[] maskAmt(int amt) {
        ByteBuffer dbuf = ByteBuffer.allocate(4);
        dbuf.order(ByteOrder.LITTLE_ENDIAN);
        dbuf.putInt(amt);
        byte[] bytes = dbuf.array(); //
        return bytes;
    }

    static int unmaskAmt(byte[] amt) {
        ByteBuffer wrapped = ByteBuffer.wrap(amt); // big-endian by default
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        int num = wrapped.getInt();
        return num;
    }

    static byte[] inv_maskAmt(int amt) {
        ByteBuffer dbuf = ByteBuffer.allocate(4);
        dbuf.order(ByteOrder.LITTLE_ENDIAN);
        dbuf.putInt(amt);
        byte[] bytes = dbuf.array(); //
        for(int x = 0; x < bytes.length; x++) {
            int curr =  ~bytes[x] & 0xff;
            bytes[x] = (byte) curr;
        }
        return bytes;
    }

    static byte[] valueblock(int amt,int block) {
        byte[] temp = new byte[16];
        byte[] value = maskAmt(amt);
        byte[] inv_value = inv_maskAmt(amt);
        byte blk = (byte)block;
        byte inv_blk = (byte) (~blk & 0xff);
        System.arraycopy(value,0,temp,0,value.length);
        System.arraycopy(inv_value,0,temp,value.length,inv_value.length);
        System.arraycopy(value,0,temp,(value.length+inv_value.length),value.length);
        temp[12]=blk;
        temp[13]=inv_blk;
        temp[14]=blk;
        temp[15]=inv_blk;
        return temp;
    }

    static int getAmtFromBlock(byte[] valueblock ) {
        byte[] temp = new byte[4];
        System.arraycopy(valueblock,0,temp,0,4);
        return unmaskAmt(temp);
    }


}
