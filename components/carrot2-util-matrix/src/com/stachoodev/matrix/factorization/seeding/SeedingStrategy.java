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

import cern.colt.matrix.*;

/**
 * Defines the seeding routine to be used as part of a matrix factorisation
 * algorithm.
 * 
 * @author Stanislaw Osinski
 * @version $Revision$
 */
public interface SeedingStrategy
{

    /**
     * Initialises values of the provided U and V matrices. The A matrix is the
     * input matrix to be factorized.
     * 
     * @param A matrix to be factorized
     * @param U
     * @param V
     */
    public void seed(DoubleMatrix2D A, DoubleMatrix2D U, DoubleMatrix2D V);
}