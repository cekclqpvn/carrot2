/*
 * Carrot2 Project
 * Copyright (C) 2002-2005, Dawid Weiss
 * Portions (C) Contributors listed in carrot2.CONTRIBUTORS file.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the CVS checkout or at:
 * http://www.cs.put.poznan.pl/dweiss/carrot2.LICENSE
 */
package com.stachoodev.matrix.factorization.seeding;

/**
 * Creates random seeding strategies.
 * 
 * @author Stanislaw Osinski
 * @version $Revision$
 */
public class RandomSeedingStrategyFactory implements SeedingStrategyFactory
{
    /** The random seed to be used */
    private int seed;

    /** If true, current system time will be used as the random seed */
    private boolean dateSeed;

    /**
     * Creates the factory with seeding based on current system time.
     */
    public RandomSeedingStrategyFactory()
    {
        this.dateSeed = true;
    }

    /**
     * Creates the factory with given seed value.
     * 
     * @param i
     */
    public RandomSeedingStrategyFactory(int seed)
    {
        this.seed = seed;
        this.dateSeed = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.stachoodev.matrix.factorization.seeding.SeedingStrategyFactory#createSeedingStrategy()
     */
    public SeedingStrategy createSeedingStrategy()
    {
        RandomSeedingStrategy seeding;

        if (dateSeed)
        {
            seeding = new RandomSeedingStrategy();
        }
        else
        {
            seeding = new RandomSeedingStrategy(seed);
        }

        return seeding;
    }

    /**
     * Returns the random seed to be used.
     * 
     * @return
     */
    public int getSeed()
    {
        return seed;
    }

    /**
     * Sets the random seed to be used. Disables seeding with current system
     * time.
     * 
     * @param seed
     */
    public void setSeed(int seed)
    {
        this.seed = seed;
        this.dateSeed = false;
    }

    /**
     * Returns true if the current system time is used to generate seed.
     * 
     * @return
     */
    public boolean getDateSeed()
    {
        return dateSeed;
    }

    /**
     * Set date seed to true to use current system time as random seed.
     * 
     * @param dateSeed
     */
    public void setDateSeed(boolean dateSeed)
    {
        this.dateSeed = dateSeed;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "R";
    }
}