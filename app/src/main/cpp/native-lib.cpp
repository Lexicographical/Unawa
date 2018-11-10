#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>

using namespace std;
using namespace cv;

void modifyMatBoundaries(JNIEnv *env, Mat &mat, jintArray hsvBoundsGreen, jintArray hsvBoundsRed, jint colorMode, jboolean showBinary);
extern "C"
JNIEXPORT void JNICALL
Java_com_gl_unawa_listeners_CVListener_modifyMat(JNIEnv *env, jobject instance,
                                        jlong matAddr, jintArray hsvBoundsGreen, jintArray hsvBoundsRed, jint colorMode, jboolean showBinary) {

    Mat &mat = *(Mat *) matAddr;
//    cvtColor(mat, mat, COLOR_BGR2GRAY);
//    cv::Canny(mat, mat, 100, 3, 3);
    modifyMatBoundaries(env, mat, hsvBoundsGreen, hsvBoundsRed, colorMode, showBinary);
//    rectangle(mat, cv::Point(120, 0), cv::Point(840, 720), cv::Scalar(0, 255, 0));
    GaussianBlur(mat, mat, Size(11, 11), 0, 0);
    cvtColor(mat, mat, COLOR_GRAY2BGRA);
}

void modifyMatBoundaries(JNIEnv *env, Mat &mat, jintArray hsvBoundsGreen, jintArray hsvBoundsRed, jint colorMode, jboolean showBinary) {
    cvtColor(mat, mat, COLOR_BGR2HSV);
    jint* arrGreen = env->GetIntArrayElements(hsvBoundsGreen, 0);
    jint* arrRed = env->GetIntArrayElements(hsvBoundsRed, 0);

    if (showBinary && colorMode != 1) {
        jint* arr = arrGreen;
        if (colorMode == 3) {
            arr = arrRed;
        }
        inRange(mat, Scalar(arr[0], arr[2], arr[4]), Scalar(arr[1], arr[3], arr[5]), mat);
    } else {
        Mat mRed, mGreen;
        inRange(mat, Scalar(arrGreen[0], arrGreen[2], arrGreen[4]), Scalar(arrGreen[1], arrGreen[3], arrGreen[5]), mGreen);
        inRange(mat, Scalar(arrRed[0], arrRed[2], arrRed[4]), Scalar(arrRed[1], arrRed[3], arrRed[5]), mRed);
        bitwise_or(mRed, mGreen, mat);
    }
}