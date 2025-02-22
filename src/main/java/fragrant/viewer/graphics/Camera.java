package fragrant.viewer.graphics;

import com.jogamp.opengl.GL2;

public class Camera {
    private float x = 0, y = 0, z = 0;
    private float rotX = 30, rotY = 45;
    private float distance = 50;

    private boolean movingForward, movingBack;
    private boolean movingLeft, movingRight;
    private boolean movingUp, movingDown;

    public void moveRelative(float dx, float dy, float dz) {
        double yawRad = Math.toRadians(rotY);
        x += (float) (dx * Math.cos(yawRad) - dz * Math.sin(yawRad));
        y += dy;
        z += (float) (dx * Math.sin(yawRad) + dz * Math.cos(yawRad));
    }

    public void applyViewTransform(GL2 gl) {
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -distance);
        gl.glRotatef(rotX, 1, 0, 0);
        gl.glRotatef(rotY, 0, 1, 0);
        gl.glTranslatef(-x, -y, -z);
    }

    public void centerOn(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void adjustRotation(float dx, float dy) {
        rotY += dx * 0.5f;
        rotX += dy * 0.5f;
        rotX = Math.max(-89, Math.min(89, rotX));
    }

    public void adjustDistance(float delta) {
        distance += delta;
        distance = Math.max(5, distance);
    }

    public void setMovingForward(boolean moving) { movingForward = moving; }
    public void setMovingBack(boolean moving) { movingBack = moving; }
    public void setMovingLeft(boolean moving) { movingLeft = moving; }
    public void setMovingRight(boolean moving) { movingRight = moving; }
    public void setMovingUp(boolean moving) { movingUp = moving; }
    public void setMovingDown(boolean moving) { movingDown = moving; }

    public void update() {
        float moveSpeed = 1.0f;
        if (movingForward) moveRelative(0, 0, -moveSpeed);
        if (movingBack) moveRelative(0, 0, moveSpeed);
        if (movingLeft) moveRelative(-moveSpeed, 0, 0);
        if (movingRight) moveRelative(moveSpeed, 0, 0);
        if (movingUp) moveRelative(0, moveSpeed, 0);
        if (movingDown) moveRelative(0, -moveSpeed, 0);
    }
}