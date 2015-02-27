package com.ivonhoe.parser;

import java.util.List;

/**
 * Created by ivonhoe on 15-1-27.
 */
public abstract class Writer {

    public abstract void onWrite(List<Unit> list);

    public abstract void close();
}
