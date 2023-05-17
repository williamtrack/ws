#define _CRT_SECURE_NO_WARNINGS
#include "CHTime.h"

#include <iostream>

//跨平台/秒
void getCurrentDateTime(char* current_datetime)
{
    time_t nowtime;
    struct tm* timeinfo;
    time(&nowtime);
    timeinfo = localtime(&nowtime);
    int xtn = timeinfo->tm_year + 1900;
    xtn = xtn % 2000;
    int xty = timeinfo->tm_mon + 1;
    int xtr = timeinfo->tm_mday;
    int xts = timeinfo->tm_hour;
    int xtf = timeinfo->tm_min;
    int xtm = timeinfo->tm_sec;
    snprintf(current_datetime, 18, "%d-%d-%d %d:%d:%d", xtn, xty, xtr, xts, xtf, xtm);
}


void CHTime::pt()
{
    gettm(getTimeStamp());
}

void CHTime::pt(const char* tag)
{
    std::cout << tag << ": ";
    gettm(getTimeStamp());
}

std::string CHTime::gt()
{
    time_t milli = getTimeStamp() + (std::time_t)8 * 60 * 60 * 1000;
    auto mTime = std::chrono::milliseconds(milli);
    auto tp = std::chrono::time_point<std::chrono::system_clock, std::chrono::milliseconds>(mTime);
    auto tt = std::chrono::system_clock::to_time_t(tp);

    struct tm now;
    gmtime_s(&now, &tt);
    std::string result
        = std::to_string(now.tm_min) + ":"
        + std::to_string(now.tm_sec) + ":"
        + std::to_string(milli % 1000);
    return result;
}

std::time_t CHTime::getTimeStamp()
{
    std::chrono::time_point<std::chrono::system_clock, std::chrono::milliseconds> tp = std::chrono::time_point_cast<std::chrono::milliseconds>(std::chrono::system_clock::now());
    auto tmp = std::chrono::duration_cast<std::chrono::milliseconds>(tp.time_since_epoch());
    std::time_t timestamp = tmp.count();
    //std::time_t timestamp = std::chrono::system_clock::to_time_t(tp);
    return timestamp;
}

std::tm* CHTime::gettm(std::time_t timestamp)
{
    std::time_t milli = timestamp + (std::time_t)8*60*60*1000;//此处转化为东八区北京时间，如果是其它时区需要按需求修改
    auto mTime = std::chrono::milliseconds(milli);
    auto tp = std::chrono::time_point<std::chrono::system_clock, std::chrono::milliseconds>(mTime);
    auto tt = std::chrono::system_clock::to_time_t(tp);

    struct tm now;
    gmtime_s(&now, &tt);
    printf("%02d:%02d:%02d.%03d\n", now.tm_hour, now.tm_min, now.tm_sec, (int)(milli % 1000));
    //printf("%03lld\n", milli);
    
    //std::tm* now = std::gmtime(&tt);
    //printf("%4d年%02d月%02d日 %02d:%02d:%02d.%d\n", now->tm_year + 1900, now->tm_mon + 1, now->tm_mday, now->tm_hour, now->tm_min, now->tm_sec, milli % 1000);
    return &now;
}

int64_t CHTime::getTImeMicros()
{
#ifdef _WIN32
    // 从1601年1月1日0:0:0:000到1970年1月1日0:0:0:000的时间(单位100ns)
#define EPOCHFILETIME   (116444736000000000UL)
    FILETIME ft;
    LARGE_INTEGER li;
    int64_t tt = 0;
    GetSystemTimeAsFileTime(&ft);
    li.LowPart = ft.dwLowDateTime;
    li.HighPart = ft.dwHighDateTime;
    // 从1970年1月1日0:0:0:000到现在的微秒数(UTC时间)
    tt = (li.QuadPart - EPOCHFILETIME) / 10;
    return tt;
#else
    timeval tv;
    gettimeofday(&tv, 0);
    return (int64_t)tv.tv_sec * 1000000 + (int64_t)tv.tv_usec;
#endif // _WIN32
    return 0;
}
