package com.util;

/**
 * Created by Sang on 15/10/23.
 */
public class CalculateUtil {
    
    public static double getResultWithStartingPrice(int amount,int startingPrice,int startingAmount,int unitPrice,double unitAmount){
        if(amount<=startingAmount){
            return startingPrice;
        }else {
            return Math.ceil((amount-startingAmount)/unitAmount)*unitPrice+startingPrice;
        }
    }

    public static int getMaxOut(int sSize1 ,int sSize2,int a1,int b1){
        int aa=a1/sSize1;
        int bb=b1/sSize2;
        int ab=a1/sSize2;
        int ba=b1/sSize1;
        int num1=aa*bb;
        int num2=ab*ba;
        int num=Math.max(num1, num2);
        int leftA=a1*b1-num*sSize1*sSize2;
        if(leftA>sSize1*sSize2&&num1!=0&&num2!=0){
            int numLeft=0;
            if(num==num1){
                int aal=a1%sSize1;
                int bbl=b1%sSize2;
                if(Math.max(aal,bbl)>sSize2){
                    if(aal>bbl){
                        numLeft=getMaxOut(sSize1,sSize2,aal,b1);
                    }else{
                        numLeft=getMaxOut(sSize1,sSize2,a1,bbl);
                    }
                }
            }else {
                int abl=a1%sSize2;
                int bal=b1%sSize1;
                if(Math.max(abl,bal)>sSize2){
                    if(abl>bal){
                        numLeft=getMaxOut(sSize1,sSize2,b1,sSize2);
                    }else{
                        numLeft=getMaxOut(sSize1,sSize2,a1,bal);
                    }
                }
            }
            num=num+numLeft;
        }
        return num;
    }

    public static int getUnitSize(double bSize1,double bSize2,int a1,int b1){
        int aNum= (int) Math.ceil(bSize1/a1);
        int bNum= (int) Math.ceil(bSize2/b1);
        int Mnum1=aNum*bNum;
        int a1Num=(int)Math.ceil(bSize1/b1);
        int b1Num=(int)Math.ceil(bSize2/a1);
        int Mnum2=a1Num*b1Num;
        return Math.min(Mnum1,Mnum2);
    }


    public static void main(String[] args) {
        System.out.println(getResultWithStartingPrice(2100,300,1000,70,1000));
    }
}
