#include <Windows.h>
#include <stdio.h>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <direct.h>

/*
ofstream：写操作
ifstream：读操作
fstream：读写操作
*/

std::string file2string(const std::string& path);

void string2file(std::string content, const std::string& path);

void file2char(const char* fileName);

void char2file(uint8_t* data, const char* fileName);


void testFile()
{
    //创建文件夹
    //std::string folderPath = "D:/aaa";
    //if (0 != _mkdir(folderPath.c_str()))
    //{
    //    // 返回 0 表示创建成功，-1 表示失败
    //    _mkdir(folderPath.c_str());
    //}

    const char* fileName = "D:/data/ws/cpp/_democpp/_democpp/test1.txt";
    std::string file_contents = "this is a test text.";
    uint8_t data[] = {
        0xff,0xff,0xff,0xff,
        0x00,0x00,0x00,0x00,
        0xff,0xff,0xff,0xff,
        0x00,0x00,0x00,0x00, };

    //string2file(file_contents, fileName);
    //std::cout << file2string(fileName) << std::endl;

    //char2file(data, fileName);
    //file2char(fileName);

}

std::string file2string(const std::string& path) {
    std::ifstream file(path);
    //判断文件是否打开/是否存在
    if (!file.is_open()) {
        std::cerr << "Could not open the file - '" << path << "'" << std::endl;
        exit(EXIT_FAILURE);
    }
    std::string res = std::string((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());
    file.close();
    return res;
}

void string2file(std::string content, const std::string& path) {
    std::ofstream file;
    file.open(path);//没有则新建？
    if (!file)
    {
        std::cerr << "Could not open the file - '" << path << "'" << std::endl;
        exit(0);
    }
    file << content;
    file.close();
    //std::cout << content << std::endl;
}

void file2char(const char* fileName) {
    FILE* ifile = NULL;
    fopen_s(&ifile, fileName, "rb");
    if (ifile == NULL)return;
    const int size = 3000;
    uint8_t inbuf[size];
    while (!feof(ifile)) {
        size_t data_size = fread(inbuf, 1, size, ifile);
        std::cout << "size " << data_size << std::endl;
        printf("%s", inbuf);
        if (!data_size) break;
    }
}


void char2file(uint8_t* data, const char* fileName) {
    FILE* pfile = NULL;
    fopen_s(&pfile, fileName, "wb");
    if (pfile) {
        fwrite(data, 16 * sizeof(unsigned char), 1, pfile);
        fclose(pfile);
    }
}
