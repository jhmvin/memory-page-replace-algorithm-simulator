package os;

public class MemoryFrame {

    public Object frameNumber;
    public JSarray memoryFrame = new JSarray();
    public Object candidate;
    public boolean result;

    public MemoryFrame(Object frameNumber, JSarray mf, Object candidate, boolean hit) {
        this.frameNumber = frameNumber;
        for (Object o : mf) {
            this.memoryFrame.push(o);
        }
        this.candidate = candidate;
        this.result = hit;
    }

    public Object getFrameNumber() {
        return this.frameNumber;
    }

    public Object getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Object o) {
        this.candidate = o;
    }

    public boolean getResult() {
        return this.result;
    }

    public JSarray getMemoryFrame() {
        return this.memoryFrame;
    }

}
