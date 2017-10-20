package com.service;

import com.dao.ParameterDao;
import com.model.Paper;
import com.model.PrintPrice;
import com.model.Size;
import com.util.CalculateUtil;
import com.web.controller.CalculateController;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Sang on 15/10/21.
 */
@Service
public class CalculatePrintingService {
    private static final int basicWaste=50;
    private static final double unitWaste=0.01;

    private static final Map<Integer,Integer[]> sizeMap=new HashMap<>();
    {
        sizeMap.put(2,new Integer[]{840,570});
        sizeMap.put(4,new Integer[]{570,420});
        sizeMap.put(8,new Integer[]{420,285});
    }
    @Resource
    private ParameterDao parameterDao;
    @Resource
    private CalculateCraftService calculateCraftService;
    @Resource
    private PriceProposalService priceProposalService;

    public double[] calculateBasic(String size,String paper,int amount,int side,String colorNum){
        Paper thisPaper=parameterDao.findPaper(paper);
        Size thisSize = parameterDao.findSize(size, side);
        double area=1.062/thisSize.getDivideNum();
        List<PrintPrice> printPriceList= new ArrayList<>();

        double minPrice=10000000;
        double minPaperPrice=1000000;
        double minMachinePrice=1000000;

        if(thisPaper.getSpecial().equals("8开")){
            //只能用8开机
            PrintPrice printPrice8 = parameterDao.findPrintPrice("8开机", colorNum);
            printPriceList.add(printPrice8);
        }else if(thisPaper.getSpecial().equals("UV")){
            //只能用UV机
            PrintPrice printPriceUV = parameterDao.findPrintPrice("UV机", "四色");
            printPriceList.add(printPriceUV);
        }else{
            //正常印刷
            PrintPrice printPrice2 = parameterDao.findPrintPrice("对开机", colorNum);
            PrintPrice printPrice4 = parameterDao.findPrintPrice("4开机", colorNum);
            PrintPrice printPrice8 = parameterDao.findPrintPrice("8开机", colorNum);
            printPriceList.addAll(Arrays.asList(printPrice2, printPrice4, printPrice8));
        }

        for(PrintPrice printPrice:printPriceList){
            if(thisSize.isThisSizeAble(printPrice.getDivideNum())){
                int printAmount = thisSize.getPrintDivide(printPrice.getDivideNum())*amount/thisSize.getDivideNum()*thisSize.getSide();
                double machinePrice=0;
                int waste=basicWaste;
                //需要两个版
                if(thisSize.getMethod(printPrice.getDivideNum()).equals("正反")){
                    waste=2*basicWaste;
                    machinePrice=CalculateUtil.getResultWithStartingPrice(printAmount,2*printPrice.getStartingPrice(),2000,printPrice.getUnitPrice(),1000);
                }else {
                    machinePrice=CalculateUtil.getResultWithStartingPrice(printAmount,printPrice.getStartingPrice(),1000,printPrice.getUnitPrice(),1000);
                }
                //纸钱
                double paperAmount=printAmount/thisSize.getSide()+Math.max(printAmount*unitWaste,basicWaste);
                double paperPrice=paperAmount*thisSize.getSize()/thisSize.getPrintDivide(printPrice.getDivideNum())*thisPaper.getWeight()/1000000*thisPaper.getWeightPrice()+amount*area*thisPaper.getAreaPrice();
                minPrice=Math.min(minPrice,machinePrice+paperPrice);
                if(minPrice==machinePrice+paperPrice){
                    minPaperPrice=paperPrice;
                    minMachinePrice=machinePrice;
                }
            }
        }
        return new double[]{minPrice, minPaperPrice};
    }

    public double[] calculateSelfInput(int length,int width,int amount,int side, String colorNum,String paper){
        int bigDivideNum=CalculateUtil.getMaxOut(length, width, 1194, 889);
        int smallDivideNum=CalculateUtil.getMaxOut(length, width, 1092,787);
        double area=1.062/bigDivideNum;
        if(bigDivideNum==smallDivideNum){
            area=0.859/smallDivideNum;
        }
        Paper thisPaper=parameterDao.findPaper(paper);

        double minPrice=10000000;
        double minPaperPrice=1000000;
        double minMachinePrice=1000000;
        List<PrintPrice> printPriceList= new ArrayList<>();

        if(thisPaper.getSpecial().equals("UV")){
            PrintPrice printPriceUV = parameterDao.findPrintPrice("UV机", "四色");
            printPriceList.add(printPriceUV);
        }else{
            //正常印刷
            PrintPrice printPrice2 = parameterDao.findPrintPrice("对开机", colorNum);
            PrintPrice printPrice4 = parameterDao.findPrintPrice("4开机", colorNum);
            PrintPrice printPrice8 = parameterDao.findPrintPrice("8开机", colorNum);
            printPriceList.addAll(Arrays.asList(printPrice2, printPrice4, printPrice8));
        }
        for(PrintPrice printPrice:printPriceList){
            int out=CalculateUtil.getMaxOut(length,width,sizeMap.get(printPrice.getDivideNum())[0],sizeMap.get(printPrice.getDivideNum())[1]);
            if(out!=0){
                int printAmount = amount/out;
                double machinePrice=CalculateUtil.getResultWithStartingPrice(printAmount,printPrice.getStartingPrice(),1000,printPrice.getUnitPrice(),1000);
                double paperArea=area*out;
                double paperAmount=printAmount+Math.max(printAmount*unitWaste,basicWaste);
                double paperPrice=paperAmount*paperArea*thisPaper.getWeight()/1000000*thisPaper.getWeightPrice()+amount*area*thisPaper.getAreaPrice();
                minPrice=Math.min(minPrice,machinePrice+paperPrice);
                if(minPrice==machinePrice+paperPrice){
                    minPaperPrice=paperPrice;
                    minMachinePrice=machinePrice;
                }
            }
        }
        return new double[]{minPrice,minPaperPrice};
    }

    public double calculatePaper(String paperName, int divideNum, double standardSize,int amount){
        Paper thisPaper=parameterDao.findPaper(paperName);
        return standardSize/divideNum*amount*thisPaper.getWeight()/1000000*thisPaper.getWeightPrice();
    }

    public double[] calculateDy(String size,String paper,int amount,int side,String colorNum,int typeAmount){
        String[] sizeParam=size.split("\\-");
        String standard = sizeParam[0];
        int divideNum=Integer.parseInt(sizeParam[1]);
        int m2amount=0;
        int m2left=0;
        int m4amount=0;
        int m4left=0;
        int m8amount=0;
        int m8left =0;
        double m2price[]=new double[2];
        double m4price[]=new double[2];
        double m8price[]=new double[2];
        double leftprice[]=new double[2];
        if(divideNum%2==0){
            m2amount=typeAmount/(divideNum/2);
            m2left=typeAmount%(divideNum/2);
            if(divideNum/4!=0){
                m4amount=m2left/(divideNum/4);
                m4left=m2left%(divideNum/4);
                if(divideNum/8!=0){
                    m8amount=m4left/(divideNum/8);
                    m8left=m4left%(divideNum/8);
                }
            }
        }else {
            Size thisSize = parameterDao.findSize(size,side);
            int to2=thisSize.getDivideNum()/thisSize.getM2printDivide();
            if(to2==1){
                //说明是3开
                m8left=typeAmount;
            }else {
                m2amount=typeAmount/to2;
                m2left=typeAmount%to2;
                if(thisSize.getM4printDivide()!=null){
                    int to4=thisSize.getDivideNum()/thisSize.getM4printDivide();
                    m4amount=m2left/to4;
                    m4left=m2left%to4;
                    if(thisSize.getM8printDivide()!=null){
                        int to8=thisSize.getDivideNum()/thisSize.getM8printDivide();
                        m8amount=m4left/to8;
                        m8left=m4left%to8;
                    }
                }
            }
        }
        if(m2amount!=0){
            String size2=standard+"-2";
            m2price=calculateBasic(size2,paper,amount,side,colorNum);
            m2price[0]*=m2amount;
            m2price[1]*=m2amount;
        }
        if(m4amount!=0){
            String size4=standard+"-4";
            m4price=calculateBasic(size4,paper,amount,side,colorNum);
            m4price[0]*=m4amount;
            m4price[1]*=m4amount;
        }
        if(m8amount!=0){
            String size8=standard+"-8";
            m8price=calculateBasic(size8,paper,amount,side,colorNum);
            m8price[0]*=m8amount;
            m8price[1]*=m8amount;
        }
        if(m8left!=0){
            leftprice=calculateBasic(size,paper,amount,side,colorNum);
            leftprice[0]*=m8left;
            leftprice[1]*=m8left;
        }
        return new double[]{m2price[0]+m4price[0]+m8price[0]+leftprice[0],m2price[1]+m4price[1]+m8price[1]+leftprice[1]};
    }

    public double[] calculateHc(String size,String coverPaper,String contentPaper,int amount,String coverColorNum,String contentColorNum,int pNum){
        //封面内页用相同的纸
        String[] sizeParam=size.split("\\-");
        String standard = sizeParam[0];
        int divideNum=Integer.parseInt(sizeParam[1])/2;
        double[] price=new double[2];
        String realSize = standard+"-"+divideNum;
        if(coverPaper.equals(contentPaper)&&coverColorNum.equals(contentColorNum)){
            int typeAmount=(pNum/4)+1;
            price=calculateDy(realSize,coverPaper,amount,2,coverColorNum,typeAmount);
        }else {
            int typeAmount=pNum/4;
            double[] cover=calculateDy(realSize,coverPaper,amount,2,coverColorNum,1);
            double[] content=calculateDy(realSize,contentPaper,amount,2,contentColorNum,typeAmount);
            price[0]=cover[0]+content[0];
            price[1]=cover[1]+content[1];
        }
        return price;
    }


//    public double calculateWTLD(String size,int lianAmount,int taoAmount,int amount){
//
//    }

    public double[] calculateBGJ(String size,String paper,int amount,int innerAmount,String fmType,int length,int width){

        double cardUnitSize=1;
        double pageUnitSize=1;
        double[] printPrice=new double[2];
        double area=0;
        switch (size){
            case "64开":cardUnitSize=4;pageUnitSize=0.25;printPrice=calculateBasic("大-64",paper,amount,1,"四色");area=1.062/64;break;
            case "32开":cardUnitSize=8;pageUnitSize=0.5;printPrice=calculateBasic("大-32",paper,amount,1,"四色");area=1.062/32;break;
            case "16开":cardUnitSize=16;pageUnitSize=1;printPrice=calculateBasic("大-16",paper,amount,1,"四色");area=1.062/16;break;
            case "8开":cardUnitSize=30;pageUnitSize=2;printPrice=calculateBasic("大-8",paper,amount,1,"四色");area=1.062/8;break;
            case "selfInput":
                cardUnitSize=CalculateUtil.getUnitSize(length+4, width+4, 94, 58);
                pageUnitSize=CalculateUtil.getUnitSize(length + 4, width + 4, 214, 289);
                if(pageUnitSize==1){
                    pageUnitSize=1.0/CalculateUtil.getMaxOut(length+4,width+4,214,289);
                }
                printPrice=calculateSelfInput(length + 2, width + 2, amount, 1, "四色", paper);
                area=(length+2)*(width+2)/1000000.0;
                break;
            default:break;
        }
        switch (fmType){
            case "不覆膜":break;
            case "覆光膜":{
                double fmPrice=calculateCraftService.getFMprice("光膜",area*amount);
                printPrice[0]+=fmPrice;
                printPrice[1]+=fmPrice;
                break;
            }case "覆哑膜":{
                double fmPrice=calculateCraftService.getFMprice("哑膜",area*amount);
                printPrice[0]+=fmPrice;
                printPrice[1]+=fmPrice;
            }
        }
        double minTablePrice=10000000;
        if(paper.equals("镜面不干胶")){
            double cardPrice=priceProposalService.getBGJprice("card",amount,fmType,cardUnitSize);
            double pagePrice=priceProposalService.getBGJprice("page", amount, fmType, pageUnitSize);

            minTablePrice=Math.min(cardPrice,pagePrice);
        }
        return new double[]{Math.min(minTablePrice,printPrice[0]),printPrice[1]};
    }

}
