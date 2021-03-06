package com.kevintyang.codeclicker.codeclicker;

/**
 * Created by Kevin on 7/10/2014.
 */
public class MoneyClickUpgrades {
    private String description;
    private int clickAddAmount;
    private int cost;
    private int maxQty;
    private int qty = 0;
    private double costMultiplier;


    public MoneyClickUpgrades(String name, int clickAddAmount, int initCost, int maxQty, double costMultiplier){
        description = name;
        clickAddAmount = clickAddAmount;
        cost = initCost;
        maxQty = maxQty;
        costMultiplier = costMultiplier;
    }

    public void buyUpgrade(){
        if(MoneyCounters.getCurrentMoneyCount() >= cost && qty <maxQty) {
            //create a pop up message on GUI

            MoneyCounters.increaseClickValue(clickAddAmount);
            qty++;

            //update upgrade view with new text

            cost = (int)(cost * costMultiplier);

            //create a GUI message that says insufficient funds(?)
        }
    }

    public long getClickValue(){

        return (long) (clickAddAmount *qty);
    }

}
