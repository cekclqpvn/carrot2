/**
 * 
 */
package org.carrot2.core.fetcher;

import java.util.ArrayList;

/**
 * A counter of remaining {@link Fetcher}s.
 * 
 * @author Dawid Weiss
 */
final class SearchResultCollector
{
    private int fetchersCount;
    private final SearchResult [] results;

    /**
     * 
     */
    public SearchResultCollector(int releaseCount)
    {
        this.fetchersCount = releaseCount;
        this.results = new SearchResult [releaseCount];
    }

    /**
     * 
     */
    public void done(int fetcherIndex, SearchResult result)
    {
        synchronized (this)
        {
            results[fetcherIndex] = result;

            if (fetchersCount > 0)
            {
                fetchersCount--;
            }
            if (fetchersCount == 0)
            {
                this.notifyAll();
            }
        }
    }

    /**
     * 
     */
    public void blockUntilZero() throws InterruptedException
    {
        synchronized (this)
        {
            while (fetchersCount > 0)
            {
                this.wait();
            }
        }
    }

    /**
     * 
     */
    public SearchResult [] getSearchResults()
    {
        // filter out only these results that contained some documents.
        final ArrayList realResults = new ArrayList(this.results.length);
        for (int i = 0; i < results.length; i++)
        {
            if (results[i].results != null)
            {
                realResults.add(results[i]);
            }
        }

        return (SearchResult []) realResults.toArray(new SearchResult [realResults.size()]);
    }
}