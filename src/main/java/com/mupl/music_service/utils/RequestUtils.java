package com.mupl.music_service.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;
import java.util.List;


public class RequestUtils {
    public static Pageable getPageable(ServerRequest request, String defaultSortBy) {
        int page = convertRequestParam(request, Integer.class, "page", 1);
        int size = convertRequestParam(request, Integer.class, "size", 10);
        String sortBy = convertRequestParam(request, String.class, "sortBy", defaultSortBy);
        String sortOrder = convertRequestParam(request, String.class, "sortOrder", "ASC");
        return PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
    }

    public static String getRequestHeader(ServerRequest request, String name) {
        if (StringUtils.isNotBlank(request.headers().firstHeader(name))) {
            return request.headers().firstHeader(name);
        }
        return null;
    }

    public static String getPathVariable(ServerRequest request, String path) {
        String value = null;
        try {
            value = request.pathVariable(path);
        } catch (IllegalArgumentException ignored) {

        }
        return value;
    }

    public static <T> T convertRequestParam(ServerRequest request, Class<T> clazz, String paramName, T defaultValue) {
        String paramValue = request.queryParam(paramName).orElse("");
        if (StringUtils.isBlank(paramValue)) {
            return defaultValue;
        }
        try {
            if (clazz == Integer.class) {
                return clazz.cast(Integer.parseInt(paramValue));
            } else if (clazz == Long.class) {
                return clazz.cast(Long.parseLong(paramValue));
            } else if (clazz == Double.class) {
                return clazz.cast(Double.parseDouble(paramValue));
            } else if (clazz == Boolean.class) {
                return clazz.cast(Boolean.parseBoolean(paramValue));
            } else if (clazz == String.class) {
                return clazz.cast(paramValue);
            } else if (clazz == List.class) {
                List<String> list = Arrays.stream(paramValue.split(","))
                        .map(String::trim)
                        .toList();
                return clazz.cast(list);
            }
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Invalid parameter value for " + paramName + ": " + paramValue);
        }
        throw new UnsupportedOperationException("Unsupported conversion type: " + clazz.getSimpleName());
    }
}
