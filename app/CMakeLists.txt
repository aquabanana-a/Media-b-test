cmake_minimum_required(VERSION 3.6.0-rc2)

add_library(
        YuvNativeMapper
        SHARED
        src/main/cpp/domain/base/mapper/YuvNativeMapper.cpp
)

add_library(
        BitmapOperationsMapper
        SHARED
        src/main/cpp/domain/base/mapper/BitmapOperationsNativeMapper.cpp
)

target_link_libraries(BitmapOperationsMapper -ljnigraphics)