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

import junit.framework.*;
import junitx.framework.*;
import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;

import com.stachoodev.matrix.*;
import com.stachoodev.matrix.factorization.seeding.*;

/**
 * @author Stanislaw Osinski
 * @version $Revision$
 */
public class MatrixFactorizationTest extends TestCase
{
    /** Factorization parameters */
    private static final int K = 2;
    private static final int MAX_ITERATIONS = 50;
    private static final double STOP_THRESHOLD = 0.0;

    /** The maximum allowed difference between matrix elelemnts */
    private double DELTA = 1e-4;

    /** The test input matrix */
    private DoubleMatrix2D A = NNIDoubleFactory2D.nni.make(new double [] []
    {
    { 0.00, 0.00, 0.56, 0.56, 0.00, 0.00, 1.00 },
    { 0.49, 0.71, 0.00, 0.00, 0.00, 0.71, 0.00 },
    { 0.49, 0.71, 0.00, 0.00, 0.00, 0.71, 0.00 },
    { 0.72, 0.00, 0.00, 0.00, 1.00, 0.00, 0.00 },
    { 0.00, 0.00, 0.83, 0.83, 0.00, 0.00, 0.00 } });

    /**
     *  
     */
    public void testNativeSVD()
    {
        NNIInterface.suppressNNI(false);
        if (NNIInterface.isNativeLapackAvailable())
        {
            PartialSingularValueDecompositionFactory factory = new PartialSingularValueDecompositionFactory();
            factory.setK(2);

            PartialSingularValueDecomposition factorization = (PartialSingularValueDecomposition) factory
                .factorize(A);

            DoubleMatrix2D expectedU = NNIDoubleFactory2D.nni
                .make(new double [] []
                {
                { 0, -0.752646 },
                { 0.651927, 0 },
                { 0.651927, 0 },
                { 0.387277, 0 },
                { 0, -0.658425 } });

            DoubleMatrix2D expectedV = NNIDoubleFactory2D.nni
                .make(new double [] []
                {
                { 0.557873, 0 },
                { 0.562741, 0 },
                { 2.775558E-017, -0.619628 },
                { -8.326673E-017, -0.619628 },
                { 0.23542, 0 },
                { 0.562741, 0 },
                { -2.220446E-016, -0.48179 } });

            double [] expectedS = new double []
            { 1.6450472, 1.5621864 };

            assertTrue("U matrix", MatrixUtils.equals(expectedU, factorization
                .getU(), DELTA));
            assertTrue("V matrix", MatrixUtils.equals(expectedV, factorization
                .getV(), DELTA));
            ArrayAssert.assertEquals("S array", expectedS, factorization
                .getSingularValues(), DELTA);
        }
    }

    /**
     *  
     */
    public void testNMFED()
    {
        NonnegativeMatrixFactorizationEDFactory factory = new NonnegativeMatrixFactorizationEDFactory();
        setParameters(factory);

        DoubleMatrix2D expectedU = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 6.1201e-011, 0.99592 },
        { 1.3886, 6.9024e-070 },
        { 1.3886, 8.2856e-070 },
        { 0.82488, 7.4263e-066 },
        { 5.8999e-011, 0.87124 } });

        DoubleMatrix2D expectedV = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 0.43087, 1.0265e-069 },
        { 0.43463, 8.1702e-070 },
        { 9.7187e-012, 0.73153 },
        { 9.7187e-012, 0.73153 },
        { 0.18182, 3.9668e-063 },
        { 0.43463, 7.7853e-070 },
        { 8.5895e-012, 0.5688 } });

        MatrixFactorization factorization = factory.factorize(A);

        assertTrue("U matrix", MatrixUtils.equals(expectedU, factorization
            .getU(), DELTA));
        assertTrue("V matrix", MatrixUtils.equals(expectedV, factorization
            .getV(), DELTA));
    }

    /**
     *  
     */
    public void testKMeans()
    {
        KMeansMatrixFactorizationFactory factory = new KMeansMatrixFactorizationFactory();
        setParameters(factory);

        DoubleMatrix2D expectedU = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 0, 0.424 },
        { 0.245, 0.284 },
        { 0.245, 0.284 },
        { 0.86, 0 },
        { 0, 0.332 } });

        DoubleMatrix2D expectedV = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 1, 0 },
        { 0, 1 },
        { 0, 1 },
        { 0, 1 },
        { 1, 0 },
        { 0, 1 },
        { 0, 1 } });

        MatrixFactorization factorization = factory.factorize(A);

        assertTrue("U matrix", MatrixUtils.equals(expectedU, factorization
            .getU(), DELTA));
        assertTrue("V matrix", MatrixUtils.equals(expectedV, factorization
            .getV(), DELTA));
    }

    /**
     *  
     */
    public void testOrderedNMFED()
    {
        NonnegativeMatrixFactorizationEDFactory factory = new NonnegativeMatrixFactorizationEDFactory();
        setParameters(factory);
        factory.setOrdered(true);

        DoubleMatrix2D expectedU = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 0.99592, 6.1201e-011 },
        { 6.9024e-070, 1.3886 },
        { 8.2856e-070, 1.3886 },
        { 7.4263e-066, 0.82488 },
        { 0.87124, 5.8999e-011 } });

        DoubleMatrix2D expectedV = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 1.0265e-069, 0.43087 },
        { 8.1702e-070, 0.43463 },
        { 0.73153, 9.7187e-012 },
        { 0.73153, 9.7187e-012 },
        { 3.9668e-063, 0.18182 },
        { 7.7853e-070, 0.43463 },
        { 0.5688, 8.5895e-012 } });

        MatrixFactorization factorization = factory.factorize(A);

        assertTrue("U matrix", MatrixUtils.equals(expectedU, factorization
            .getU(), DELTA));
        assertTrue("V matrix", MatrixUtils.equals(expectedV, factorization
            .getV(), DELTA));
    }

    /**
     *  
     */
    public void testNMFKL()
    {
        NonnegativeMatrixFactorizationKLFactory factory = new NonnegativeMatrixFactorizationKLFactory();
        setParameters(factory);

        DoubleMatrix2D expectedU = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 1.31E-11, 0.56085 },
        { 0.34477, 1.34E-12 },
        { 0.34477, 1.67E-12 },
        { 0.31047, 2.89E-12 },
        { 1.35E-11, 0.43915 } });

        DoubleMatrix2D expectedV = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 1.7, 3.18E-12 },
        { 1.42, 4.66E-12 },
        { 5.76E-11, 1.39 },
        { 5.76E-11, 1.39 },
        { 1.0, 1.08E-11 },
        { 1.42, 4.42E-12 },
        { 5.97E-11, 1.0 } });

        MatrixFactorization factorization = factory.factorize(A);

        assertTrue("U matrix", MatrixUtils.equals(expectedU, factorization
            .getU(), DELTA));
        assertTrue("V matrix", MatrixUtils.equals(expectedV, factorization
            .getV(), DELTA));
    }

    /**
     *  
     */
    public void testLNMF()
    {
        LocalNonnegativeMatrixFactorizationFactory factory = new LocalNonnegativeMatrixFactorizationFactory();
        setParameters(factory);

        DoubleMatrix2D expectedU = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 1.06E-193, 0.56085 },
        { 0.34477, 5.97E-184 },
        { 0.34477, 8.61E-184 },
        { 0.31047, 3.73E-179 },
        { 2.68E-192, 0.43915 } });

        DoubleMatrix2D expectedV = NNIDoubleFactory2D.nni.make(new double [] []
        {
        { 1.3038, 4.47E-05 },
        { 1.1916, 4.47E-05 },
        { 5.48E-05, 1.179 },
        { 5.48E-05, 1.179 },
        { 1.0, 4.47E-05 },
        { 1.1916, 4.47E-05 },
        { 5.48E-05, 1.0 }, });

        MatrixFactorization factorization = factory.factorize(A);

        assertTrue("U matrix", MatrixUtils.equals(expectedU, factorization
            .getU(), DELTA));
        assertTrue("V matrix", MatrixUtils.equals(expectedV, factorization
            .getV(), DELTA));
    }

    /**
     * @param factory
     */
    private void setParameters(IterativeMatrixFactorizationFactory factory)
    {
        factory.setK(K);
        factory.setMaxIterations(MAX_ITERATIONS);
        factory.setStopThreshold(STOP_THRESHOLD);
        factory.setSeedingFactory(new ConstantSeedingStrategyFactory());
        factory.setOrdered(false);
    }

    /** Returns constant matrices of fixed size */
    private class ConstantSeedingStrategyFactory implements
        SeedingStrategyFactory
    {
        private final double [][] constantU = new double [] []
        {
        { 0.84319, 0.6119 },
        { 0.8062, 0.10298 },
        { 0.85779, 0.15832 },
        { 0.60975, 0.41365 },
        { 0.56573, 0.56041 } };

        private final double [][] constantV = new double [] []
        {
        { 0.26868, 0.087422 },
        { 0.78425, 0.93323 },
        { 0.38787, 0.25938 },
        { 0.030984, 0.20417 },
        { 0.5855, 0.049208 },
        { 0.55856, 0.60616 },
        { 0.2007, 0.54635 } };

        /*
         * (non-Javadoc)
         * 
         * @see com.stachoodev.matrix.factorization.seeding.SeedingStrategyFactory#createSeedingStrategy()
         */
        public SeedingStrategy createSeedingStrategy()
        {
            return new SeedingStrategy()
            {

                public void seed(DoubleMatrix2D A, DoubleMatrix2D U,
                    DoubleMatrix2D V)
                {
                    U.assign(constantU);
                    V.assign(constantV);
                }

            };
        }
    }
}