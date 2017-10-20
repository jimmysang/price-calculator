package com.util.Error;

/**
 * Created by Sang on 15/11/23.
 */
public class PriceProposalNotFoundException extends Exception {
    public PriceProposalNotFoundException(){
        super("找不到价格方案");
    }
}
