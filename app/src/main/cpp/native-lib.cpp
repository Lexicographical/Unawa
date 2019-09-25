#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <cmath>
#include <android/log.h>

using namespace std;
using namespace cv;

bool compareContourAreas(std::vector<cv::Point> contour1, std::vector<cv::Point> contour2) {
    double i = fabs(contourArea(cv::Mat(contour1)));
    double j = fabs(contourArea(cv::Mat(contour2)));
    return i < j;
}

void modifyMatBoundaries(JNIEnv *env, Mat &mat, jintArray hsvBoundsGreen, jintArray hsvBoundsRed,
                         jint colorMode, jboolean showBinary);

extern "C"
JNIEXPORT void JNICALL
Java_com_gl_unawa_listeners_CVListener_modifyMat(JNIEnv *env, jobject instance,
                                                 jlong matAddr, jlong classifyAddr,
                                                 jintArray hsvBoundsGreen, jintArray hsvBoundsRed,
                                                 jint colorMode, jboolean showBinary) {

    Mat &mat = *(Mat *) matAddr;
    Mat &classify = *(Mat *) classifyAddr;
    modifyMatBoundaries(env, mat, hsvBoundsGreen, hsvBoundsRed, colorMode, showBinary);
    GaussianBlur(mat, mat, Size(11, 11), 0, 0);

//    int startX = 400, endX = startX + 300;
//    int startY = 100, endY = startY + 300;
//    Rect roi(Point(startX, startY), Point(endX, endY));
//    classify.create(roi.height, roi.width, mat.type());
//    mat(roi).copyTo(classify);
//    resize(classify, classify, Size(50, 50));
//
//    if (colorMode <= 1) {
////        detect contours if colorMode in [0, 1]
//        vector<vector<Point> > contours;
//        findContours(classify, contours, RETR_TREE, CHAIN_APPROX_NONE);
//        if (contours.size() > 0) {
//            std::sort(contours.begin(), contours.end(), compareContourAreas);
//            vector<Point> contour = contours[contours.size() - 1];
//            double area = contourArea(contour);
//            __android_log_print(ANDROID_LOG_INFO, "native-lib", "Contour max: %f", area);
//            if (area > 500) {
//                Rect bound = boundingRect(contour);
//                int height = bound.height;
//                int width = bound.width;
//                int pad = abs(width - height) / 2;
//                resize(classify, classify, Size(height, width));
//                mat(bound).copyTo(classify);
//                if (width > height) {
//                    copyMakeBorder(classify, classify, pad, pad, 0, 0, BORDER_CONSTANT, Scalar(0, 0, 0));
//                } else {
//                    copyMakeBorder(classify, classify, 0, 0, pad, pad, BORDER_CONSTANT, Scalar(0, 0, 0));
//                }
//            }
//        }
//    }

    cvtColor(mat, mat, COLOR_GRAY2BGRA);
//    cvtColor(classify, classify, COLOR_GRAY2BGRA);

}

void modifyMatBoundaries(JNIEnv *env, Mat &mat, jintArray hsvBoundsGreen, jintArray hsvBoundsRed,
                         jint colorMode, jboolean showBinary) {
    cvtColor(mat, mat, COLOR_BGR2HSV);
    jint *arrGreen = env->GetIntArrayElements(hsvBoundsGreen, 0);
    jint *arrRed = env->GetIntArrayElements(hsvBoundsRed, 0);

    if (showBinary && colorMode != 1) {
        jint *arr = arrGreen;
        if (colorMode == 3) {
            arr = arrRed;
        }
        inRange(mat, Scalar(arr[0], arr[2], arr[4]), Scalar(arr[1], arr[3], arr[5]), mat);
    } else {
        Mat mRed, mGreen;
        inRange(mat, Scalar(arrGreen[0], arrGreen[2], arrGreen[4]),
                Scalar(arrGreen[1], arrGreen[3], arrGreen[5]), mGreen);
        inRange(mat, Scalar(arrRed[0], arrRed[2], arrRed[4]),
                Scalar(arrRed[1], arrRed[3], arrRed[5]), mRed);
        bitwise_or(mRed, mGreen, mat);
    }
}