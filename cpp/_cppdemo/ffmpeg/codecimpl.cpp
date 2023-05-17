#include "codecimpl.h"
extern "C"{
    #include <libavcodec/avcodec.h>
}

#include <iostream>

namespace CH {

    int encode(AVCodecContext* ctx, const AVFrame* frame, std::shared_ptr<EncodeCallback> callback) {
        auto onSuccess = std::bind(&EncodeCallback::OnSuccess, callback, std::placeholders::_1, std::placeholders::_2);
        return CH::encode(ctx, frame, onSuccess);
    }

    int encode(AVCodecContext* ctx, const AVFrame* frame, OnEncodeSuccess onSucess) {
        int ret;
        ret = avcodec_send_frame(ctx, frame);
        if (ret < 0) {
            return -1;
        }
        AVPacket* pkt = av_packet_alloc();
        int result = 0;
        while (ret >= 0) {
            ret = avcodec_receive_packet(ctx, pkt);
            std::cout <<"=="<< ret << std::endl; //-11
            if (ret == AVERROR(EAGAIN) || ret == AVERROR_EOF) {
                break;
            }
            else if (ret < 0) {
                result = -1;
                break;
            }
            else {
                if (onSucess != nullptr)
                    onSucess(ctx, pkt);
                av_packet_unref(pkt);
            }
        }
        av_packet_free(&pkt);
        return result;
    }

    int decode(AVCodecContext* ctx, const AVPacket* packet, std::shared_ptr<DecodeCallback> callback) {
        auto onSuccess = std::bind(&DecodeCallback::OnSuccess, callback, std::placeholders::_1, std::placeholders::_2);
        return CH::decode(ctx, packet, onSuccess);
    }

    int decode(AVCodecContext* ctx, const AVPacket* packet, OnDecodeSuccess onSucess) {
        int ret = avcodec_send_packet(ctx, packet);
        if (ret < 0) {
            return -1;
        }
        AVFrame* frame = av_frame_alloc();
        int result = 0;
        while (ret >= 0) {
            ret = avcodec_receive_frame(ctx, frame);
            if (ret == AVERROR(EAGAIN) || ret == AVERROR_EOF) {
                break;
            }
            else if (ret < 0) {
                result = -1;
                break;
            }
            else {
                if (onSucess != nullptr)
                    onSucess(ctx, frame);
                av_frame_unref(frame);
            }
        }
        av_frame_free(&frame);
        return result;
    }

}