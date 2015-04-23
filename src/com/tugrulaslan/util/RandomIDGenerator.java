package com.tugrulaslan.util;

import java.util.Random;

public class RandomIDGenerator {
    
     public int newRandomID(){
        Random rand = new Random();
        return rand.nextInt(Short.MAX_VALUE )+1;
    }
    
}
