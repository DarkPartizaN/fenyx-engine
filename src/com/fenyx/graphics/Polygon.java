package com.fenyx.graphics;

import org.joml.Vector3f;

/**
 *
 * @author DarkPartizaN
 */
public class Polygon {

    public Vertex[] vertices;
    public Texture tex;

    public Polygon() {
        vertices = new Vertex[3];
    }

    public Polygon(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Vector3f calcNormal() {
        Vector3f edge1 = vertices[1].pos.sub(vertices[0].pos);
        Vector3f edge2 = vertices[2].pos.sub(vertices[0].pos);

        return edge1.cross(edge2).normalize();
    }
}
