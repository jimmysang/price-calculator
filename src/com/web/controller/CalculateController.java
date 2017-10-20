package com.web.controller;

import com.model.Decoration;
import com.service.CalculateCraftService;
import com.service.CalculatePrintingService;
import com.service.ParameterService;
import com.service.PriceProposalService;
import com.util.ApplicationContextProvider;
import com.util.Error.PriceProposalNotFoundException;
import com.util.ResponseUtil;
import com.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sang on 15/10/22.
 */
@Controller
@RequestMapping("/calculate")
public class CalculateController {
    @Resource
    private PriceProposalService priceProposalService;
    @Resource
    private CalculatePrintingService calculatePrintingService;
    @Resource
    private CalculateCraftService calculateCraftService;
    @Autowired
    private ApplicationContext appContext;
    @Resource
    private ParameterService parameterService;

    private final Map<String,Double> standardSize=new HashMap<>();
    private final Map<String,Decoration> decorationMap = new HashMap<>();

    @PostConstruct
    private void init(){
        standardSize.put("大", 1.062);
        standardSize.put("正",0.859);
        standardSize.put("特",1.11125);
        List<Decoration> decorationList = parameterService.listDecoration();
        decorationList.forEach(decoration -> decorationMap.put(decoration.getType(),decoration));
    }
    @RequestMapping("/test")
    private void test(HttpServletRequest request,HttpServletResponse response){
        ResponseUtil.write(response,"test",null);
    }

    @RequestMapping("/dy")
    private void calculateDy(HttpServletRequest request,HttpServletResponse response){
        System.out.println(request.getSession());
        JSONObject result = new JSONObject();
        String paperName=request.getParameter("paperName");
        String paperWeight=request.getParameter("paperWeight");
        String paper=paperName+paperWeight;
        String size=request.getParameter("size");
        System.out.println(size);
        String colorNum=request.getParameter("colorNum");
        String customerId=request.getParameter("customerId");
        int side=0;
        int amount=0;
        int typeAmount=0;
        try {
            side=Integer.parseInt(request.getParameter("side"));
            amount=Integer.parseInt(request.getParameter("amount"));
            typeAmount=Integer.parseInt(request.getParameter("typeAmount"));
        }catch (Exception e){
            result.put("error","您输入的数字不规范！");
        }
        String isZY=request.getParameter("isZY");
        String isYH=request.getParameter("isYH");
        String isFM=request.getParameter("isFM");
        String zyType=request.getParameter("zyType");
        String fmType=request.getParameter("fmType");
        double[] price=new double[2];
        double craftPrice=0;
        try {
            String standerd=size.split("\\-")[0];
            int divideNum=Integer.parseInt(size.split("\\-")[1]);
            double area = standardSize.get(standerd)/divideNum;
            price = calculatePrintingService.calculateDy(size, paper, amount, side, colorNum, typeAmount);
            if(paperName.equals("铜版纸")&&!standerd.equals("特")&&divideNum>=4){
                double[] proposalPrice=priceProposalService.getDyPrice(paperWeight, side == 1 ? "单" : "双", amount, customerId,typeAmount*16.0/divideNum);
                if(proposalPrice[0]<price[0]){
                    price=proposalPrice;
                }
            }

            if(isFM!=null){
                String material=fmType.split("\\-")[0];
                int fmSide=Integer.parseInt(fmType.split("\\-")[1]);
                craftPrice+=calculateCraftService.getFMprice(material,fmSide*area*amount)*typeAmount;
            }
            if(isYH!=null){
                craftPrice+=calculateCraftService.getYHprice(amount);
            }
            if(isZY!=null){
                craftPrice+=calculateCraftService.getZYprice(zyType,divideNum,amount);
            }
        }catch (Exception e){
            result.put("error","抱歉，参数有误或不支持计算!");
            e.printStackTrace();
        }
        int resultPrice=(int)(price[0]+craftPrice);
        int resultCost=(int)(price[1]+craftPrice);
        Decoration decoration = decorationMap.get("单页");
        resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
        result.put("price",resultPrice);
        result.put("cost",resultCost);
        ResponseUtil.write(response,result,request.getParameter("callback"));
    }

    @RequestMapping("/hc")
    private void calculateHc(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        String size=request.getParameter("size");
        String paperCoverName=request.getParameter("paperCoverName");
        String paperContentName=request.getParameter("paperContentName");
        String paperCoverWeight=request.getParameter("paperCoverWeight");
        String paperContentWeight=request.getParameter("paperContentWeight");
        String coverPaper=paperCoverName+paperCoverWeight;
        String contentPaper=paperContentName+paperContentWeight;
        String isAT=request.getParameter("isAT");
        String isUV=request.getParameter("isUV");
        String isFM=request.getParameter("isFM");
        String fmType=request.getParameter("fmType");
        String isTJ=request.getParameter("isTJ");
        String isYH=request.getParameter("isYH");
        String bind=request.getParameter("bind");
        String coverColorNum=request.getParameter("coverColorNum");
        String contentColorNum=request.getParameter("contentColorNum");
        String customerId=request.getParameter("customerId");
        int side=0;
        int amount=0;
        int pNum=0;
        double atArea=0;
        double uvArea=0;
        double tjArea=0;
        try {
            amount=Integer.parseInt(request.getParameter("amount"));
            pNum=Integer.parseInt(request.getParameter("pNum"));
            atArea=Double.parseDouble(request.getParameter("atArea"));
            uvArea=Double.parseDouble(request.getParameter("uvArea"));
            tjArea=Double.parseDouble(request.getParameter("tjArea"));
        }catch (ClassCastException e){
            result.put("error", "您输入的数字不规范!");
        }
        try {
            double[] price = calculatePrintingService.calculateHc(size,coverPaper,contentPaper,amount,coverColorNum,contentColorNum,pNum);
            double craftPrice=0;
            if(isAT!=null){
                craftPrice+=calculateCraftService.getATprice(amount, atArea / 1000000, true);
            }
            if(isUV!=null){
                craftPrice+=calculateCraftService.getUVprice(amount, uvArea / 1000000);
            }
            if(isFM!=null){
                String standerd=size.split("\\-")[0];
                int divideNum=Integer.parseInt(size.split("\\-")[1]);
                String material=fmType.split("\\-")[0];
                int fmSide=Integer.parseInt(fmType.split("\\-")[1]);
                double area = standardSize.get(standerd)/divideNum*2;
                craftPrice+=calculateCraftService.getFMprice(material, fmSide * area * amount);
            }
            if(isTJ!=null){
                craftPrice+=calculateCraftService.getTJprice(amount, tjArea / 1000000, true);
            }
            if(isYH!=null){
                craftPrice+=calculateCraftService.getYHprice(amount);
            }

            craftPrice+=calculateCraftService.getZDprice(bind,amount,pNum+4);

            int resultPrice=(int)(price[0]+craftPrice);
            int resultCost=(int)(price[1]+craftPrice);
            Decoration decoration = decorationMap.get("画册");
            resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
            result.put("price",resultPrice);
            result.put("cost",resultCost);
        }catch (Exception e){
            e.printStackTrace();
            result.put("error","抱歉，参数有误或不支持计算!");
        }
        ResponseUtil.write(response,result,request.getParameter("callback"));
    }

    @RequestMapping("/mp")
    private void calculateMp(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        double[] price=new double[2];
        double craftPrice=0;
        String paperName=request.getParameter("paperName");
        String isDM=request.getParameter("isDM");
        String isYH=request.getParameter("isYH");
        String isDK=request.getParameter("isDK");
        String isMQ=request.getParameter("isMQ");
        String mqPlate=request.getParameter("mqPlate");
        String isYJ=request.getParameter("isYJ");
        String isAT=request.getParameter("isAT");
        String atPlate=request.getParameter("atPlate");
//        String isUV=request.getParameter("isUV");
        String uvPlate=request.getParameter("uvPlate");
        String isTJ=request.getParameter("isTJ");
        String tjPlate=request.getParameter("tjPlate");
        String customerId=request.getParameter("customerId");
        try {
            int unitSize=Integer.parseInt(request.getParameter("unitSize"));
            int side=Integer.parseInt(request.getParameter("side"));
            int amount=Integer.parseInt(request.getParameter("amount"));
            int typeAmount=Integer.parseInt(request.getParameter("typeAmount"));
            int tjSide=Integer.parseInt(request.getParameter("tjSide"));
            try {
                price=priceProposalService.getMpPrice(paperName,side==1?"单":"双",amount,unitSize*typeAmount,customerId);
            } catch (SQLException e) {
                e.printStackTrace();
                result.put("error","抱歉，服务器出错，请联系客服报价");
            } catch (PriceProposalNotFoundException e) {
                result.put("error","抱歉，该数量目前没有活动，再多印一点吧!");
            }
            if(isDM!=null){
                craftPrice+=calculateCraftService.getMPDMprice(amount);
            }
            if(isYH!=null){
                craftPrice+=calculateCraftService.getMPYHprice(amount);
            }
            if(isDK!=null){
//                price+=calculateCraftService.get
            }
            if(isMQ!=null){
                craftPrice+=calculateCraftService.getMPMQprice(amount, mqPlate.equals("有版") ? false : true, unitSize);
            }
            if(isYJ!=null){
                craftPrice+=calculateCraftService.getMPYJprice(amount);
            }
            if(isAT!=null){
                craftPrice+=calculateCraftService.getMPATprice(amount, atPlate.equals("有版") ? false : true);
            }
            if(isTJ!=null){
                craftPrice+=calculateCraftService.getMPTJprice(amount, side, tjPlate.equals("有版") ? false : true);
            }
            int resultPrice=(int)(price[0]+craftPrice);
            int resultCost=(int)(price[1]+craftPrice);
            Decoration decoration = decorationMap.get("名片");
            resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
            result.put("price",resultPrice);
            result.put("cost",resultCost);
        }catch (ClassCastException e){
            result.put("error","您输入的数字不规范！");
        }
        ResponseUtil.write(response,result,request.getParameter("callback"));
    }

    @RequestMapping("/std")
    private void calculateStd(HttpServletRequest request,HttpServletResponse response){
        JSONObject result= new JSONObject();
        String size=request.getParameter("size");
        String paper=request.getParameter("paper");
        String isAT=request.getParameter("isAT");
        String isUV=request.getParameter("isUV");
        String isFM=request.getParameter("isFM");
        String isMD=request.getParameter("isMD");
        String isDJ=request.getParameter("isDJ");
        String rope=request.getParameter("rope");
        String fmType=request.getParameter("fmType");
        String isTJ=request.getParameter("isTJ");
        String customerId=request.getParameter("customerId");
        int height=height=StringUtil.toInt(request.getParameter("height"));
        int width=width=StringUtil.toInt(request.getParameter("width"));
        int thickness=thickness=StringUtil.toInt(request.getParameter("thickness"));
        int amount=0;
        double atArea=0;
        double uvArea=0;
        double tjArea=0;
        double area =0;
        try {
            amount=Integer.parseInt(request.getParameter("amount"));
            atArea=Double.parseDouble(request.getParameter("atArea"));
            uvArea=Double.parseDouble(request.getParameter("uvArea"));
            tjArea=Double.parseDouble(request.getParameter("tjArea"));
        }catch (ClassCastException e){
            result.put("error","您输入的数字不规范!");
        }
        double[] price=new double[2];
        double craftPrice=0;
        if(size.equals("selfInput")){
            int length2D=height+60+thickness/2;
            int width2D=(width+thickness)*2+20;
            area = length2D*width2D/1000000.0;
            price=calculatePrintingService.calculateSelfInput(length2D,width2D,amount,1,"四色",paper);
        }else {
            price=calculatePrintingService.calculateBasic(size, paper, amount, 1, "四色");
        }
        if(isAT!=null){
            craftPrice+=calculateCraftService.getATprice(amount, atArea / 1000000, true);
        }
        if(isUV!=null){
            craftPrice+=calculateCraftService.getUVprice(amount, uvArea / 1000000);
        }
        if(isFM!=null){
            if(area==0){
                String standerd=size.split("\\-")[0];
                int divideNum=Integer.parseInt(size.split("\\-")[1]);
                area = standardSize.get(standerd)/divideNum;
            }
            String material=fmType.split("\\-")[0];
            int fmSide=Integer.parseInt(fmType.split("\\-")[1]);
            craftPrice+=calculateCraftService.getFMprice(material, fmSide * area * amount);
        }
        if(isTJ!=null){
            craftPrice+=calculateCraftService.getTJprice(amount, tjArea / 1000000, true);
        }
        if(isMD!=null){
            craftPrice+=calculateCraftService.getMD(amount);
        }
        if(isDJ!=null){
            craftPrice+=calculateCraftService.getDJprice(amount);
        }
        switch (rope){
            case "精品绳":craftPrice+=calculateCraftService.getJPSprice(amount);break;
            case "棉绳":craftPrice+=calculateCraftService.getCMSprice(amount);break;
                default:break;
        }
        craftPrice+=calculateCraftService.getSTDprice(amount,size.equals("selfInput"));
        int resultPrice=(int)(price[0]+craftPrice);
        int resultCost=(int)(price[1]+craftPrice);
        Decoration decoration = decorationMap.get("手提袋");
        resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
        result.put("price",resultPrice);
        result.put("cost",resultCost);
        ResponseUtil.write(response,result,request.getParameter("callback"));

    }

    @RequestMapping("/ft")
    private void calculateFt(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        String size=request.getParameter("size");
        String paper=request.getParameter("paper");
        String isAT=request.getParameter("isAT");
        String isUV=request.getParameter("isUV");
        String isFM=request.getParameter("isFM");
        String fmType=request.getParameter("fmType");
        String isTJ=request.getParameter("isTJ");
        String isYH=request.getParameter("isYH");
        String isKC=request.getParameter("isKC");
        int side=0;
        int amount=0;
        int pNum=0;
        double atArea=0;
        double uvArea=0;
        double tjArea=0;
        try {
            side=Integer.parseInt(request.getParameter("side"));
            amount=Integer.parseInt(request.getParameter("amount"));
        }catch (Exception e){
            result.put("error","您输入的数字不规范！");
        }
        double[] price=calculatePrintingService.calculateBasic(size, paper, amount, side, "四色");
        double craftPrice=0;
        if(isAT!=null){
            craftPrice+=calculateCraftService.getATprice(amount, atArea / 1000000, true);
        }
        if(isUV!=null){
            craftPrice+=calculateCraftService.getUVprice(amount, uvArea / 1000000);
        }
        if(isFM!=null){
            String standerd=size.split("\\-")[0];
            int divideNum=Integer.parseInt(size.split("\\-")[1]);
            String material=fmType.split("\\-")[0];
            int fmSide=Integer.parseInt(fmType.split("\\-")[1]);
            double area = standardSize.get(standerd)/divideNum;
            craftPrice+=calculateCraftService.getFMprice(material, fmSide * area * amount);
        }
        if(isTJ!=null){
            craftPrice+=calculateCraftService.getTJprice(amount, tjArea / 1000000, true);
        }
        if(isYH!=null){
            craftPrice+=calculateCraftService.getYHprice(amount);
        }
        if(isKC!=null){
//            price+=calculateCraftService.getk.
        }
        craftPrice+=calculateCraftService.getFTprice(amount);
        int resultPrice=(int)(price[0]+craftPrice);
        int resultCost=(int)(price[1]+craftPrice);
        Decoration decoration = decorationMap.get("封套");
        resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
        result.put("price",resultPrice);
        result.put("cost",resultCost);
        ResponseUtil.write(response,result,request.getParameter("callback"));
    }

    @RequestMapping("/xf")
    private void calculateXf(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        String size=request.getParameter("size");
        String paper=request.getParameter("paper");
        String colorNum=request.getParameter("colorNum");
        String isJS=request.getParameter("isJS");
        String jsType=request.getParameter("jsType");
        String isKTC=request.getParameter("isKTC");
        String isFM=request.getParameter("isFM");
        String fmType=request.getParameter("fmType");
        String isTJ=request.getParameter("isTJ");
        int length=StringUtil.toInt(request.getParameter("length"));
        int width=StringUtil.toInt(request.getParameter("width"));
        int amount=0;
        double tjArea=0;
        double area=0;
        String sizeNum=null;
        if(size.equals("selfInput")){
            sizeNum="自定义";
        }else {
            sizeNum=size.split("\\-")[2];
        }
        int paperWeight=Integer.parseInt(paper.replaceAll("\\D", ""));
        try {
            tjArea=Integer.parseInt(request.getParameter("tjArea"));
            amount=Integer.parseInt(request.getParameter("amount"));
        }catch (Exception e){
            result.put("error","您输入的数字不规范！");
        }
        double[] price=new double[2];
        if(size.equals("selfInput")){
            area=length*width/1000000.0;
            price=calculatePrintingService.calculateSelfInput(length,width,amount,1,colorNum,paper);
        }else {
            price=calculatePrintingService.calculateBasic(size, paper, amount, 1, colorNum);
        }
        double craftPrice=0;
        if(isTJ!=null){
            craftPrice+=calculateCraftService.getTJprice(amount, tjArea / 1000000, true);
        }
        if(isJS!=null){
            craftPrice+=calculateCraftService.getJSprice(amount, jsType);
        }
        if(isKTC!=null){
            craftPrice+=calculateCraftService.getKTCprice(amount);
        }
        if(isFM!=null){
            if(area==0){
                String standerd=size.split("-")[0];
                int divideNum=Integer.parseInt(size.split("\\-")[1]);
                area = standardSize.get(standerd)/divideNum;
            }
            String material=fmType.split("\\-")[0];
            int fmSide=Integer.parseInt(fmType.split("\\-")[1]);
            craftPrice+=calculateCraftService.getFMprice(material, fmSide * area * amount);
        }
        craftPrice+=calculateCraftService.getXFprice(paperWeight, isFM != null, sizeNum, amount);
        int resultPrice=(int)(price[0]+craftPrice);
        int resultCost=(int)(price[1]+craftPrice);
        Decoration decoration = decorationMap.get("信封");
        resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
        result.put("price",resultPrice);
        result.put("cost",resultCost);
        ResponseUtil.write(response,result,request.getParameter("callback"));
    }

    @RequestMapping("/wtld")
    private void calculateWtld(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        String size=request.getParameter("size");
        String isTY=request.getParameter("isTY");
        String paper=request.getParameter("paper");
        int amount=0;
        int lianAmount=0;
        int taoAmount=0;
        int colorNum=0;
        try {
            amount=Integer.parseInt(request.getParameter("amount"));
            lianAmount=Integer.parseInt(request.getParameter("lianAmount"));
            taoAmount=Integer.parseInt(request.getParameter("taoAmount"));
            colorNum=Integer.parseInt(request.getParameter("colorNum"));
        }catch (Exception e){
            result.put("error","您输入的数字不规范！");
        }
        double tablePrice=priceProposalService.getWTprice(size,lianAmount,taoAmount,amount);
        double[] price=calculatePrintingService.calculateBasic(size, paper, lianAmount*taoAmount*amount, 1, "单色");
        price[0] += (price[0]-price[1])*(colorNum-1);
        double craftPrice=0;
        if(isTY!=null){
            craftPrice+=30*(lianAmount-1);
        }
        int resultPrice=(int)(price[0]+craftPrice);
        int resultCost=(int)(price[1]+craftPrice);
        Decoration decoration = decorationMap.get("无碳联单");
        resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
        result.put("price",resultPrice);
        result.put("cost",resultCost);
        ResponseUtil.write(response,result,request.getParameter("callback"));
    }

    @RequestMapping("/bgj")
    private void calculateBgj(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        String size=request.getParameter("size");
        String paper=request.getParameter("paper");
        String fmType=request.getParameter("fmType");
        String cut=request.getParameter("cut");
        int length=StringUtil.toInt(request.getParameter("length"));
        int width=StringUtil.toInt(request.getParameter("width"));
        int amount=0;
        int innerAmount=0;
        try {
            amount= Integer.parseInt(request.getParameter("amount"));
            innerAmount=Integer.parseInt(request.getParameter("innerAmount"));
        }catch (Exception e){
            result.put("error","您输入的数字不规范！");
        }
        double[] price=calculatePrintingService.calculateBGJ(size, paper, amount, innerAmount, fmType, length, width);
        double craftPrice=0;
        if(size.equals("selfInput")){
            int realWidth=Math.min(length,width);
            int realLength=Math.max(length,width);
            if(length<=840&&width<=570)size="对开";
            if(length<=570&&width<=420)size="四开";
            if(length<=420&&width<=285)size="8开";
            if(length<=285&&width<=210)size="16开";
            if(length<=210&&width<=140)size="32开";
            if(length<=140&&width<=105)size="64开";
            if(length<=90&&width<=54)size="名片";
        }
        switch (cut){
            case "切成品":case "留毛尺寸":break;
            case "模切":craftPrice+=calculateCraftService.getMQprice("不干胶",size,innerAmount,amount);break;
            case "划线":craftPrice+=calculateCraftService.getHXprice(amount);break;
        }
        int resultPrice=(int)(price[0]+craftPrice);
        int resultCost=(int)(price[1]+craftPrice);
        Decoration decoration = decorationMap.get("不干胶");
        resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
        result.put("price",resultPrice);
        result.put("cost",resultCost);
        ResponseUtil.write(response,result,request.getParameter("callback"));
    }

    @RequestMapping("/tl")
    private void calculateTl(HttpServletRequest request,HttpServletResponse response){
        JSONObject result = new JSONObject();
        String paperName=request.getParameter("paperName");
        String paperWeight=request.getParameter("paperWeight");
        String paper=paperName+paperWeight;
        // size 格式为 正-6-正 正-6表示架子的尺寸，后一个正表示内容纸的规格
        String size=request.getParameter("size");
        String style=request.getParameter("style");
        String basePaper=request.getParameter("basePaper");
        String isPrintBase =request.getParameter("isPrintBase");
        String customerId=request.getParameter("customerId");
        int amount=0;
        double tjArea=0;
        try {
            amount=Integer.parseInt(request.getParameter("amount"));
            tjArea=Integer.parseInt(request.getParameter("tjArea"));
        }catch (Exception e){
            result.put("error","您输入的数字不规范！");
        }
        String isTJ=request.getParameter("isTJ");
        String isFM=request.getParameter("isFM");
        String fmType=request.getParameter("fmType");
        double[] price=new double[2];
        String contentStandard = size.split("\\-")[2];
        String standerd=size.split("\\-")[0];
        int divideNum=Integer.parseInt(size.split("\\-")[1]);
        if(style.equals("7张")){
            price = calculatePrintingService.calculateBasic(contentStandard+"-4",paper,amount,2,"四色");
        }else {
            price = calculatePrintingService.calculateBasic(contentStandard+"-2",paper,amount,2,"四色");
        }
        if(isPrintBase!=null){
            double[] basePrintPrice = calculatePrintingService.calculateBasic(size.substring(0,size.length()-2),"铜版纸157",amount,1,"四色");
            price[0] += basePrintPrice[0];
            price[1] += basePrintPrice[1];
        }else {
            if(basePaper.equals("艺术纸")){
                price[0] += 0.2*amount;
                price[1] += 0.2*amount;
            }else {
                double basePaperPrice=calculatePrintingService.calculatePaper("铜版纸157",divideNum,standardSize.get(standerd),amount);
                price[0] += basePaperPrice;
                price[1] += basePaperPrice;
            }
        }
        double craftPrice=0;
        try {
            if(isFM!=null){
                double area = standardSize.get(standerd)/divideNum;
                String material=fmType.split("\\-")[0];
                int fmSide=Integer.parseInt(fmType.split("\\-")[1]);
                craftPrice+=calculateCraftService.getFMprice(material, fmSide * area * amount);
            }
            if(isTJ!=null){
                craftPrice+=calculateCraftService.getTJprice(amount, tjArea / 1000000, true);
            }
            craftPrice+=calculateCraftService.getTLprice(amount);
        }catch (Exception e){
            result.put("error","抱歉，参数有误或不支持计算!");
            e.printStackTrace();
        }
        int resultPrice=(int)(price[0]+craftPrice);
        int resultCost=(int)(price[1]+craftPrice);
        Decoration decoration = decorationMap.get("台历");
        resultPrice=resultPrice<decoration.getStartValue()?resultPrice:(int)(resultPrice*(1+decoration.getRatio()));
        result.put("price",resultPrice);
        result.put("cost",resultCost);
        ResponseUtil.write(response,result,request.getParameter("callback"));

    }



}
