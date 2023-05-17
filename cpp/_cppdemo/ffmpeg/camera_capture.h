#pragma once

extern "C" {
#include <libavformat/avformat.h>
#include <libavdevice/avdevice.h>
#include <libavutil/time.h>
#include "libavutil/avutil.h"
}

#include <string>
#include <iostream>

class camera_capture
{
public:
    camera_capture();
    bool Load(const std::string& url, int width, int height);
    void Release();

    AVFrame* NextFrame();

private:
    AVFrame* frame;
    //vector<AVFrame> frames;
    AVPacket* packet;
    AVFormatContext* fmt_ctx = NULL;
    AVCodecContext* enc_ctx = NULL;
    AVPacket* newpkt;
    int width;
    int height;
    FILE* outfile;

    int ret = 0;
    int base = 0;
    int count = 0;
};

