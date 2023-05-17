#include "stream_encoder.h"

stream_encoder::stream_encoder()
{
}



int stream_encoder::load(OnEncodeSuccess callback, AVCodecID codec_id,int width, int height)
{
    this->callback = callback;
    //int width = 640;
    //int height = 480;

    codec = avcodec_find_encoder(codec_id);
    //codec = avcodec_find_encoder_by_name("libx264");

    pCodecCtx = avcodec_alloc_context3(codec);

    //InitVideoAVCodecCtx(pCodecCtx, codec_id, width, height);
    setPara(&pCodecCtx, codec_id, width, height);

    ret = avcodec_open2(pCodecCtx, codec, NULL);
    if (ret < 0) {
        fprintf(stderr, "Could not open codec: %s\n", av_err2str(ret));
        exit(1);
    }

    frame = av_frame_alloc();
    return 0;
}

int stream_encoder::flush()
{
    CH::encode(pCodecCtx, nullptr, callback);

    avcodec_close(pCodecCtx);
    av_frame_free(&frame);

    //fclose(rFile);
    //fclose(oFile);

    printf("完成输出\n");
    return 0;
}

int stream_encoder::encode(AVFrame* avframe)
{
    //int i = 0;
    //frame->pts = i++;
    CH::encode(pCodecCtx, avframe, callback);
    //av_frame_unref(avframe);  //注意如果释放，camera_capture进程frame会出错
    return 0;
}

void stream_encoder::setPara(AVCodecContext** enc_ctx, AVCodecID codecId, int width, int height)
{
    (*enc_ctx)->codec_id = codecId;
    (*enc_ctx)->codec_type = AVMEDIA_TYPE_VIDEO;

    // SPS/PPS
    //(*enc_ctx)->profile = FF_PROFILE_H264_HIGH_444;
    //(*enc_ctx)->profile = FF_PROFILE_HEVC_MAIN;
    //(*enc_ctx)->level = 50; //表示LEVEL是5.0

    //设置分辫率
    (*enc_ctx)->width = width;   // 640
    (*enc_ctx)->height = height; // 480

    //// GOP
    //(*enc_ctx)->gop_size = 250;
    //(*enc_ctx)->keyint_min = 25; // option

    //设置B帧数据
    (*enc_ctx)->max_b_frames = 0; // 3
    (*enc_ctx)->has_b_frames = 0; // 1

    //参考帧的数量
    (*enc_ctx)->refs = 3; // option

    //设置输入YUV格式
    (*enc_ctx)->pix_fmt = AV_PIX_FMT_YUV420P;
    //(*enc_ctx)->pix_fmt = AV_PIX_FMT_YUV422P;

    //设置码率
    (*enc_ctx)->bit_rate = 1000000; // 600kbps

    //设置帧率
    //AVRational a = { 1,30 };
    (*enc_ctx)->time_base = { 1, 30 }; //帧与帧之间的间隔是time_base
    (*enc_ctx)->framerate = { 30, 1 }; //帧率，每秒 30帧
}
