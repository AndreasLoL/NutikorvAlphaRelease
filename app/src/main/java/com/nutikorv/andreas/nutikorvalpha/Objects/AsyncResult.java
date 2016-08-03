package com.nutikorv.andreas.nutikorvalpha.Objects;

/**
 * Created by ANDREAS on 26.07.2016.
 */
import org.json.JSONObject;

public interface AsyncResult
{
    void onResult(JSONObject object);
}