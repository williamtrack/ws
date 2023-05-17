#pragma once
#include <string>

extern "C" {
#include <libavformat/avformat.h>
#include <libavdevice/avdevice.h>
#include <libavutil/time.h>
}

class VideoCapture {
public:
    VideoCapture();
    bool Load(const std::string& url);
    bool LoadCam(const std::string& url);
    void Release();

    AVFrame* NextFrame();

    void DumpVideoInfo();
    int GetVideoWidth();
    int GetVideoHeight();
    AVPixelFormat GetPixelFormat();

private:
    AVFormatContext* mFormatContext;
    AVCodecContext* mCodecContext;
    AVPacket* mPacket;
    AVFrame* mFrame;

    bool init();

    std::string mUrl;
    int mVideoStreamIndex;
    int mVideoWidth;
    int mVideoHegiht;
    int64_t mDecodecStart;
    int64_t mLastDecodecTime;
    AVPixelFormat mPixelFormat;
};


