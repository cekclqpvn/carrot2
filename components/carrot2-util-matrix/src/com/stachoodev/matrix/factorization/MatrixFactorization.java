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
package com.stachoodev.matrix.factorization;

import cern.colt.matrix.*;

/**
 * For an <i>m </i> &times; <i>n </i> matrix <i>A </i> and given <i>k </i>,
 * computes an <i>m </i> &times; <i>k </i> matrix <i>U </i> and <i>k </i>
 * &times; <i>n </i> matrix <i>V' </i> such that <i>A ~= UV' </i>.
 * 
 * @author Stanislaw Osinski
 * @version $Revision$
 */
public interface MatrixFactorization
{
    /**
     * Returns the U matrix (base vectors matrix).
     * 
     * @return U matrix
     */
    public abstract DoubleMatrix2D getU();

    /**
     * Returns the V matrix (coefficient matrix)
     * 
     * @return V matrix
     */
    public abstract DoubleMatrix2D getV();
}