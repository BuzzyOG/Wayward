package net.wayward_realms.waywardlib.util.vector;

/**
 * Utility class for vector methods
 */
public class VectorUtils {

    /**
     * Checks if a ray intersects with a box
     *
     * @param p1 the starting point of the ray
     * @param p2 the ending point of the ray
     * @param min the minimum point of the box
     * @param max the maximum point of the box
     * @return whether the ray intersects with the box
     */
    public static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;
        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
        return !(Math.abs(c.x) > e.x + ad.x || Math.abs(c.y) > e.y + ad.y || Math.abs(c.z) > e.z + ad.z || Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon || Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon || Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon);
    }

}
