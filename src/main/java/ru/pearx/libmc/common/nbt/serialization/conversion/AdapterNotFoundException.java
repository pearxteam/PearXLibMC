package ru.pearx.libmc.common.nbt.serialization.conversion;

/*
 * Created by mrAppleXZ on 03.03.18 20:13.
 */
public class AdapterNotFoundException extends RuntimeException
{
    private Class requestedClass;

    public AdapterNotFoundException(Class requestedClass)
    {
        this.requestedClass = requestedClass;
    }

    public AdapterNotFoundException(String message, Class requestedClass)
    {
        super(message);
        this.requestedClass = requestedClass;
    }

    public AdapterNotFoundException(String message, Throwable cause, Class requestedClass)
    {
        super(message, cause);
        this.requestedClass = requestedClass;
    }

    public AdapterNotFoundException(Throwable cause, Class requestedClass)
    {
        super(cause);
        this.requestedClass = requestedClass;
    }

    public AdapterNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class requestedClass)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.requestedClass = requestedClass;
    }

    public Class getRequestedClass()
    {
        return requestedClass;
    }
}
