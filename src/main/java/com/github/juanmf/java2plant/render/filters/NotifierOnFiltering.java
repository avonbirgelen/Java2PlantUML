package com.github.juanmf.java2plant.render.filters;

import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.render.event.RenderEvent;

/**
 * @param <T> Must be the same type used as parameter to the Filter for which this notifier is being set.
 *
 * @author juanmf@gmail.com
 */
public class NotifierOnFiltering <T> {

    private final boolean notify;
    private final OnResult onResult;
    private RenderEvent<T> event;

    public NotifierOnFiltering(OnResult onResult, RenderEvent<T> event) {
        this.onResult = onResult;
        this.event = event;
        this.notify = true;
    }

    public NotifierOnFiltering() {
        onResult = null;
        this.notify = false;
    }

    boolean getResultAndNotify(boolean filterResult, T filteringObject, StringBuilder sb) {
        if (! notify) {
            return filterResult;
        }
        event.setScriptStringBuilder(sb);
        try {
            event.setFilteringObject(filteringObject);
            onResult.fire(filterResult, event);
        } catch (ClassCastException ex) {
            // do nothing
        }
        return filterResult;
    }

    public enum OnResult {
        SUCCESS(true), FAILURE(false);
        boolean result;

        OnResult(boolean result) {
            this.result = result;
        }

        void fire(boolean filterResult, RenderEvent event) {
            if (this.result == filterResult) {
                Parser.getEventBus().post(event);
            }
        }
    }
}
