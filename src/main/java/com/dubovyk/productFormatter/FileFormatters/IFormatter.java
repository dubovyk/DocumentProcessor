package com.dubovyk.productFormatter.FileFormatters;

import java.util.List;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public interface IFormatter {
    List<String[]> proceed(List<String[]> inputData);
}
