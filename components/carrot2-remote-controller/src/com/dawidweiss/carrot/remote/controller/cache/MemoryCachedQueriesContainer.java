

/*
 * Carrot2 Project
 * Copyright (C) 2002-2005, Dawid Weiss
 * Portions (C) Contributors listen in carrot2.CONTRIBUTORS file.
 * All rights reserved.
 * 
 * Refer to full text of the licence "carrot2.LICENCE" in the root folder
 * of CVS checkout or at: 
 * http://www.cs.put.poznan.pl/dweiss/carrot2.LICENCE
 */


package com.dawidweiss.carrot.remote.controller.cache;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dawidweiss.carrot.util.common.StreamUtils;


/**
 * Queries are cached in memory. Size limit is restricted.
 */
public class MemoryCachedQueriesContainer
    implements CachedQueriesContainer
{
    private static final Logger log = Logger.getLogger(MemoryCachedQueriesContainer.class);
    private long sizeLimit = 1024 * 1024; //  1mb limit default
    private long currentSize = 0;
    private HashMap cache = new HashMap();
    private LinkedList lru = new LinkedList();
    private List listeners = new LinkedList();

    public void setSizeLimit(int limit)
    {
        log.debug("Setting memory limit to " + limit);
        this.sizeLimit = limit;
    }


    public void configure()
    {
    }


    public Iterator getCachedElementSignatures()
    {
        synchronized (this.cache)
        {
            return new ArrayList(lru).iterator();
        }
    }


    public void expungeFromCache(Object signature)
    {
        synchronized (this.cache)
        {
            MemoryCachedQuery cq;

            if ((cq = (MemoryCachedQuery) cache.remove(signature)) != null)
            {
                this.lru.remove(signature);
                this.currentSize -= cq.getDataSize();
                log.debug("Expunging element from cache.");
            }
        }
    }


    public CachedQuery addToCache(CachedQuery q)
    {
        synchronized (this.cache)
        {
            CachedQuery cq;

            if ((cq = (CachedQuery) cache.get(q.getSignature())) != null)
            {
                return cq;
            }

            try
            {
                if (q instanceof MemoryCachedQuery)
                {
                    cq = q;
                }
                else
                {
                    byte [] data = StreamUtils.readFullyAndCloseInput(q.getData());
                    cq = new MemoryCachedQuery(
                            q.getQuery(), q.getComponentId(), q.getOptionalParams(), data
                        );
                }
            }
            catch (IOException e)
            {
                log.error("Cannot create cached query: " + e.toString());

                return cq;
            }

            this.cache.put(cq.getSignature(), cq);
            this.lru.addFirst(cq.getSignature());
            this.currentSize += ((MemoryCachedQuery) cq).getDataSize();

            checkLimits();

            return cq;
        }
    }


    private void checkLimits()
    {
        while ((this.currentSize > this.sizeLimit) && (this.cache.size() > 0))
        {
            // pick the victim amd remove it.
            Object signature = lru.getLast();
            CachedQuery cq = this.getCachedElement(signature);

            for (Iterator i = this.listeners.iterator(); i.hasNext();)
            {
                ((CacheListener) i.next()).elementRemovedFromCache(cq);
            }

            this.expungeFromCache(signature);
        }
    }


    public CachedQuery getCachedElement(Object signature)
    {
        synchronized (this.cache)
        {
            return (CachedQuery) cache.get(signature);
        }
    }


    public Iterator getCacheListeners()
    {
        synchronized (this.cache)
        {
            return this.listeners.iterator();
        }
    }


    public void addCacheListener(CacheListener l)
    {
        synchronized (this.cache)
        {
            listeners.add(l);
        }
    }


    public void removeCacheListener(CacheListener l)
    {
        synchronized (this.cache)
        {
            listeners.remove(l);
        }
    }
}
