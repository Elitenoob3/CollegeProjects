import cv2 as cv
import numpy as nm


# Getting colored flame mask
def color_area(video_frame, video_base_level=None):
    # Define fire color levels
    low_fire = (15, 50, 100)
    high_fire = (60, 255, 255)

    # Changing the lower threshold for video calibration
    if video_base_level is not None:
        if video_base_level[1] > 50:
            low_fire = (15, video_base_level[1], 100)
        if video_base_level[2] > 100:
            low_fire = (15, low_fire[1], video_base_level[2])

    # Get HSV frame
    hsv_frame = cv.cvtColor(video_frame, cv.COLOR_BGR2HSV)

    # Get Color Mask
    local_mask = cv.inRange(hsv_frame, low_fire, high_fire)
    result = cv.bitwise_and(video_frame, video_frame, None, local_mask)

    # White flame Correction
    ret, upper_limit = cv.threshold(video_frame, 240, 255, cv.THRESH_BINARY)
    result = cv.add(result, upper_limit)
    result = cv.cvtColor(result, cv.COLOR_BGR2GRAY)
    return result


# Processing the area of the fire for contour creation
def area_processing(local_mask):
    # Blurring and dilating kernel chosen by experimentation
    kernel = nm.ones((3, 3), nm.uint8)

    # Erode and then threshold to create a more noise resistant mask
    result = cv.morphologyEx(local_mask, cv.MORPH_OPEN, kernel, 2)
    ret, result = cv.threshold(result, 60, 255, cv.THRESH_BINARY)
    result = cv.dilate(result, kernel, 2)
    return result


# Load video into file
name = 'Home_Test'
capture = cv.VideoCapture(name + '.mp4')

# Calibrate average color level to limit false positives
size = (640, 360)
isTrue, frame = capture.read()
resized_frame = cv.resize(frame, size, cv.INTER_AREA)

average = resized_frame.mean(0).mean(0)
average = (15, nm.round(average[1]+1.5, 1), nm.round(average[2]+1.5, 1))

# Keep a global flame size comparator and verifier
PastFlameSize = 1
FrameChangeCounter = 5
isFireSign = 0

FireConfidence = 0
FireConfidenceBuffer = []
MaxConfidence = 0

# Start capture on filters
area_video = cv.VideoWriter(name+'_area.avi', cv.VideoWriter_fourcc('M', 'J', 'P', 'G'), 30, size)
contour_video = cv.VideoWriter(name+'_contour.avi', cv.VideoWriter_fourcc('M', 'J', 'P', 'G'), 30,  size)

file = open('data.txt', 'w')

# Play video frame by frame
while capture.isOpened():
    isFireSign = 0

    ret, frame = capture.read()

    if ret:

        # Resize all video data to 640x360 for convenience and compatibility
        resized_frame = cv.resize(frame, (640, 360), cv.INTER_AREA)

        area = color_area(resized_frame, average)

        # Get Colored Area Ratio for fire colored pixel percentage
        ratio = cv.countNonZero(area) / (resized_frame.size / 3)
        ratio *= 100

        # Test for ratio
        # print(ratio)

        mask = nm.zeros(resized_frame.shape, dtype="uint8")

        FlameSize = 0

        # If the fire color covers more than 2.5% of an image proceed with the rest of the computation
        if ratio > 2.5:
            mask = area_processing(area)

            # Find contours of the flames
            contours, hierarchies = cv.findContours(mask, cv.RETR_LIST, cv.CHAIN_APPROX_SIMPLE)
            for x in contours:
                i = cv.contourArea(x)

                # Filter out small contours
                if i > 100:
                    resized_frame = cv.drawContours(resized_frame, [x], -1, (255, 0, 0), 2)
                    FlameSize += i

            # Calculate the check values
            FlameFluctuation = FlameSize - PastFlameSize
            FlameGrowth = FlameSize / PastFlameSize

            # Decrease the frame counter if possible
            if FrameChangeCounter > 0:
                FrameChangeCounter -= 1

            # If the flame does not fluctuate withing a small margin of error in the past 5 frames
            # we are dealing with either controlled fire, noise or static light sources
            if 80 > FlameFluctuation > -80:
                FrameChangeCounter = 5

            # If the area of fire changes in a reasonable margin, and we are not dealing with false positives
            if (1.5 > FlameGrowth > 0.50) and FrameChangeCounter == 0:
                isFireSign = 1

            # Update the flame size frame by frame
            if FlameSize > 0:
                PastFlameSize = FlameSize
            else:
                PastFlameSize = 1

        # Since Frame by frame fluctuation is not the best indicator we will use a confidence level based on the
        # detection of the last 50 frames
        if len(FireConfidenceBuffer) < 50:
            FireConfidenceBuffer.append(isFireSign)
        else:
            FireConfidenceBuffer.pop(0)
            FireConfidenceBuffer.append(isFireSign)

        FireConfidence = nm.sum(FireConfidenceBuffer)
        FireConfidence /= 50

        file.write(str(FireConfidence)+'\n')

        print(FireConfidence)
        if FireConfidence > MaxConfidence:
            MaxConfidence = FireConfidence

        # Saving Video Alterations
        area = cv.cvtColor(area, cv.COLOR_GRAY2BGR)
        area_video.write(area)
        contour_video.write(resized_frame)

        # Display the masks
        cv.imshow('Area', area)
        cv.imshow('Mask', mask)
        cv.imshow('Contours', resized_frame)

    key = cv.waitKey(12)

    if key == ord('q'):
        break

print('Maximum Fire confidence was: ' + str(MaxConfidence))
file.write('Maximum Fire confidence was: ' + str(MaxConfidence))

file.close()
capture.release()

area_video.release()
contour_video.release()

cv.destroyAllWindows()