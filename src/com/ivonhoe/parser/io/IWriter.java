package com.ivonhoe.parser.io;

import com.ivonhoe.parser.Unit;

import java.util.List;

/**
 * Created by ivonhoe on 15-1-27.
 */
public abstract class IWriter {

    public abstract void onWrite(List<Unit> list);

    public abstract void close();
}
