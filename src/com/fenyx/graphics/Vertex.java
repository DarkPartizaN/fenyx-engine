package com.fenyx.graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author DarkPartizaN
 */
public class Vertex {

    public Vector3f origin;
    public Vector3f pos;
    public Vector3f norm;
    public Vector2f uv;
    public Color color;

    public Vertex() {
        origin = new Vector3f();
        pos = new Vector3f();
        norm = new Vector3f();
        uv = new Vector2f();
    }

    public Vertex(float x, float y, float z) {
        origin = new Vector3f(x, y, z);
        pos = new Vector3f(x, y, z);
        norm = new Vector3f();
        uv = new Vector2f();
    }
}