import cv2
import mediapipe as mp
import csv
import os

# --- Ask for metadata ---
doctor_name = input("Enter Doctor Name: ").strip().replace(" ", "_")
patient_name = input("Enter Patient Name: ").strip().replace(" ", "_")
joint_name = input("Enter Joint Name (e.g., left_elbow, right_knee): ").strip().lower()

# --- Define mapping of joint names to landmark IDs (MediaPipe Pose) ---
JOINT_MAP = {
    "nose": 0, "left_eye_inner": 1, "left_eye": 2, "left_eye_outer": 3,
    "right_eye_inner": 4, "right_eye": 5, "right_eye_outer": 6,
    "left_ear": 7, "right_ear": 8,
    "mouth_left": 9, "mouth_right": 10,
    "left_shoulder": 11, "right_shoulder": 12,
    "left_elbow": 13, "right_elbow": 14,
    "left_wrist": 15, "right_wrist": 16,
    "left_pinky": 17, "right_pinky": 18,
    "left_index": 19, "right_index": 20,
    "left_thumb": 21, "right_thumb": 22,
    "left_hip": 23, "right_hip": 24,
    "left_knee": 25, "right_knee": 26,
    "left_ankle": 27, "right_ankle": 28,
    "left_heel": 29, "right_heel": 30,
    "left_foot_index": 31, "right_foot_index": 32
}

if joint_name not in JOINT_MAP:
    print(f"Error: '{joint_name}' is not a valid joint name.")
    print("Valid joints:", ", ".join(JOINT_MAP.keys()))
    exit()

joint_id = JOINT_MAP[joint_name]
HUMAN_HEIGHT_CM = 175  # for normalization

# --- Prepare CSV output ---
output_dir = "pose_data"
os.makedirs(output_dir, exist_ok=True)
csv_filename = f"{doctor_name}_{patient_name}_{joint_name}.csv"
csv_path = os.path.join(output_dir, csv_filename)

csv_file = open(csv_path, mode='w', newline='')
csv_writer = csv.writer(csv_file)
csv_writer.writerow(["frame_no", "joint_name", "x_cm", "y_cm", "z_cm"])

# --- Initialize MediaPipe Pose ---
mp_drawing = mp.solutions.drawing_utils
mp_pose = mp.solutions.pose

cap = cv2.VideoCapture(0)
if not cap.isOpened():
    print("Error: Could not open camera.")
    exit()

frame_no = 0

with mp_pose.Pose(
        static_image_mode=False,
        model_complexity=1,
        enable_segmentation=False,
        min_detection_confidence=0.5,
        min_tracking_confidence=0.5
) as pose:

    print("\n--- Pose tracking started ---")
    print(f"Tracking joint: {joint_name}")
    print(f"Saving data to: {csv_path}")
    print("Press 'q' to stop.\n")

    while True:
        ret, frame = cap.read()
        if not ret:
            print("Failed to grab frame.")
            break

        rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = pose.process(rgb_frame)
        frame_no += 1

        if results.pose_landmarks:
            mp_drawing.draw_landmarks(
                frame,
                results.pose_landmarks,
                mp_pose.POSE_CONNECTIONS,
                mp_drawing.DrawingSpec(color=(0, 255, 0), thickness=2, circle_radius=2),
                mp_drawing.DrawingSpec(color=(255, 0, 0), thickness=2, circle_radius=2)
            )

            joint = results.pose_landmarks.landmark[joint_id]
            x_cm = joint.x * HUMAN_HEIGHT_CM
            y_cm = joint.y * HUMAN_HEIGHT_CM
            z_cm = joint.z * HUMAN_HEIGHT_CM

            csv_writer.writerow([frame_no, joint_name, round(x_cm, 2), round(y_cm, 2), round(z_cm, 2)])

            cv2.putText(frame, f"{joint_name}: x={x_cm:.1f}cm y={y_cm:.1f}cm z={z_cm:.1f}cm",
                        (20, 40), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 255), 2)

        cv2.imshow("Volenii Pose Tracker", frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

cap.release()
csv_file.close()
cv2.destroyAllWindows()

print("\n✅ Tracking stopped.")
print(f"CSV file saved successfully at: {csv_path}")
