package com.yong.taximeter.domain.model

/**
 * Region Setting
 * - [key]: Unique key for each region
 */
enum class RegionSetting(
    val key: String,
) {
    SEOUL("seoul"),
    GANGWON("gangwon"),
    GYEONGGI("gyeonggi"),
    GYEONGBUK("gyeongbuk"),
    GYEONGNAM("gyeongnam"),
    GWANGJU("gwangju"),
    DAEGU("daegu"),
    DAEJEON("daejeon"),
    BUSAN("busan"),
    ULSAN("ulsan"),
    INCHEON("incheon"),
    JEONBUK("jeonbuk"),
    JEONNAM("jeonnam"),
    JEJU("jeju"),
    CHUNGBUK("chungbuk"),
    CHUNGNAM("chungnam"),
    CUSTOM("custom"),
}