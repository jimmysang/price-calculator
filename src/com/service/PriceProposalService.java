package com.service;

import com.dao.ParameterDao;
import com.dao.PriceProposalDao;
import com.util.Error.PriceProposalNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sang on 15/10/21.
 */
@Service
public class PriceProposalService {
    private static final List<Integer> dyAmountLadder=Arrays.asList(0,500,1000,2000,3000,5000);
    private static final List<Integer> mpAmountLadder=Arrays.asList(0,2,5,10,50);
    private static final List<Integer> wtldAmountLadder=Arrays.asList(0,100,200,300);
    private static final List<Integer> bgjAmountLadder=Arrays.asList(0,200,500,1000,2000,3000,4000,5000);
    @Resource
    private PriceProposalDao priceProposalDao;
    @Resource
    private ParameterDao parameterDao;

    public double[] getDyPrice(String paperWeight,String faceNum,int amount,String customerId,double unitSize)throws SQLException{
        if(unitSize<1){
            amount*=unitSize;
        }
        String code = faceNum+"面DTB"+paperWeight+"1"+amountFilter(dyAmountLadder,amount);
        double[] price = priceProposalDao.getPriceByCode(code, customerId);
        price[0]*=Math.ceil(unitSize);
        price[1]*=Math.ceil(unitSize);
        return price;
    }

    public double[] getMpPrice(String paperName,String side,int amount,int unitSize,String customerId)throws SQLException,PriceProposalNotFoundException{
        double[] price=priceProposalDao.getPrice(paperName,side,amountFilter(mpAmountLadder,amount),customerId);
        price[0]*=unitSize;
        price[1]*=unitSize;
        return price;

    }

    public double getWTprice(String size,int lianAmount,int taoAmount,int amount){
        return parameterDao.findCarbonSheetPrice(size,lianAmount,taoAmount,amountFilter(wtldAmountLadder,amount));
    }

    public double getBGJprice(String type,int amount,String fmType,double unitSize){
        if(unitSize<1){
            amount*=unitSize;
        }
        return Math.ceil(unitSize)*parameterDao.findStickerPrice(type,amountFilter(bgjAmountLadder,amount),fmType);
    }

    private int amountFilter(List<Integer> amountLadder,double amount){
        for (int i = 1; i < amountLadder.size(); i++) {
            if(amount>amountLadder.get(i-1)&&amount<=amountLadder.get(i)){
                amount=amountLadder.get(i);
                return (int)amount;
            }
        }
        int max=amountLadder.get(amountLadder.size() -1);
        return (int)Math.ceil(amount/max)*max;
    }

}
