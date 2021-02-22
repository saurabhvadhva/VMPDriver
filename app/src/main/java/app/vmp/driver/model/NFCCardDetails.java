package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NFCCardDetails {

    @SerializedName("NFCCardId")
    @Expose
    private String NFCCardId = "";

    @SerializedName("NFCTagId")
    @Expose
    private String NFCTagId = "";

    @SerializedName("BalanceAmount")
    @Expose
    private String BalanceAmount = "";

    @SerializedName("CardHolderName")
    @Expose
    private String CardHolderName = "";

    @SerializedName("NGOId")
    @Expose
    private String NGOId = "";

    @SerializedName("NFCCardNumber")
    @Expose
    private String NFCCardNumber = "";

    public String getNFCCardId() {
        return NFCCardId;
    }

    public void setNFCCardId(String NFCCardId) {
        this.NFCCardId = NFCCardId;
    }

    public String getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        BalanceAmount = balanceAmount;
    }

    public String getCardHolderName() {
        return CardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        CardHolderName = cardHolderName;
    }

    public String getNGOId() {
        return NGOId;
    }

    public void setNGOId(String NGOId) {
        this.NGOId = NGOId;
    }

    public String getNFCCardNumber() {
        return NFCCardNumber;
    }

    public void setNFCCardNumber(String NFCCardNumber) {
        this.NFCCardNumber = NFCCardNumber;
    }

    public String getNFCTagId() {
        return NFCTagId;
    }

    public void setNFCTagId(String NFCTagId) {
        this.NFCTagId = NFCTagId;
    }
}
