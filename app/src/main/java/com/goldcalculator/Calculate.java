package com.goldcalculator;

public class Calculate {

    public double goldKind(String goldKind) {
        double goldRate = 1;

        switch (goldKind) {
            case "14k":
                goldRate = 0.6435;
                break;
            case "18k":
                goldRate = 0.825;
                break;
            case "24k":
                goldRate = 1;
                break;
        }

        return goldRate;
    }

    public double goodsPrice(Double goldRate,Double doubleGoldMount,Integer intGoldMarketCondition,Integer intGoldMargin,Integer intGoldWage) {
        return goldRate * doubleGoldMount * (intGoldMarketCondition / 3.75) + intGoldMargin + intGoldWage;
    }

    public double storePrice(Double doubleCalculatedPrice){
        return doubleCalculatedPrice*1.4;
    }

    public double realMargin(Integer salePrice, Double goldRate,
                             Integer intMarketCondition,Integer intChangeMarketCondition
                             ,Double doubleMount,Double doubleChangeMount,
                             Integer intWage,Integer intChangeWage) {

        double salePriceWithoutMargin  = (goldRate * doubleMount * (intMarketCondition / 3.75) + intWage);
        double changePrice =  (goldRate * doubleChangeMount * (intChangeMarketCondition / 3.75) + intChangeWage);

        return salePrice - (changePrice - salePriceWithoutMargin);
    }


}
