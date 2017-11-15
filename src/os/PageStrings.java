package os;

import java.util.ArrayList;
import java.util.Collections;

public class PageStrings {

    private ArrayList<Object> data = new ArrayList<Object>();
    private ArrayList<MemoryFrame> frames = new ArrayList<MemoryFrame>();
    private JSarray currentFrame = new JSarray();
    private int frameCount = 0;
    private int algorithm_id = 0;

    public PageStrings(ArrayList<Object> data, int frameCount) {
        this.data = data;
        this.frameCount = frameCount;
    }
    static int last_candidate = 0;

    public void fix_index() {
        last_candidate = 0;
        boolean exist = true;
        int randomdigit = 0;
        while (exist) {
            int count = 0;
            randomdigit = (int) (Math.random() * 10);
            for (Object o : currentFrame) {
                if (randomdigit == Integer.parseInt(o.toString())) {
                    count++;
                }
            }
            if (count == 0) {
                break;
            }
        }
        data.add(randomdigit);
        if (algorithm_id == 1) {
            fifo_Counter();
        } else if (algorithm_id == 2) {
            fifo_Stack();
        } else if (algorithm_id == 3) {
            lru_Counter();
        } else if (algorithm_id == 4) {
            lru_Stack();
        } else if (algorithm_id == 5) {
            optimal_page_replacement();
        } else {
            System.out.println("CANNOT APPLY FIX NO ALGORITHM SET");
        }
        last_candidate = Integer.parseInt(frames.get(data.size() - 1).getCandidate().toString());
        data.remove(data.size() - 1);

        if (algorithm_id == 1) {
            fifo_Counter();
        } else if (algorithm_id == 2) {
            fifo_Stack();
        } else if (algorithm_id == 3) {
            lru_Counter();
        } else if (algorithm_id == 4) {
            lru_Stack();
        } else if (algorithm_id == 5) {
            optimal_page_replacement();
        } else {
            System.out.println("CANNOT APPLY FIX NO ALGORITHM SET");
        }

        for (int x = 1; x < data.size(); x++) {
            frames.get(x - 1).setCandidate(frames.get(x).getCandidate());
        }
        frames.get(data.size() - 1).setCandidate(last_candidate);

    }

    public ArrayList<MemoryFrame> getMemoryFrame() {
        return this.frames;
    }

    public void fifo_Counter() {
        algorithm_id = 1;
        currentFrame = new JSarray();
        frames = new ArrayList<MemoryFrame>();
        int candidate = 0;
        int candidateValue = 0;
        boolean hit = false;
        for (int x = 0; x < data.size(); x++) {
            if (currentFrame.size() < frameCount) {
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    currentFrame.push(data.get(x));
                    hit = false;
                } else {
                    hit = true;
                }
                candidateValue = Integer.parseInt(currentFrame.get(0).toString());
            } else {
                if (currentFrame.indexOf(data.get(x)) == -1) {

                    candidateValue = Integer.parseInt(currentFrame.get(candidate).toString());
                    currentFrame.set(candidate, data.get(x));
                    candidate++;

                    if (candidate > (frameCount - 1)) {
                        candidate = 0;
                    }

                    hit = false;
                } else {
                    candidateValue = Integer.parseInt(currentFrame.get(candidate).toString());
                    hit = true;
                    if (candidate > (frameCount - 1)) {
                        candidate = 0;
                    }
                }
            }
            //-------------
            frames.add(new MemoryFrame((x + 1), this.currentFrame, candidateValue, hit));
        }//end of loop

    }

    public void fifo_Stack() {
        algorithm_id = 2;
        currentFrame = new JSarray();
        frames = new ArrayList<MemoryFrame>();
        int candidate = 0;
        int candidateValue = 0;
        boolean hit = false;
        for (int x = 0; x < data.size(); x++) {
            if (currentFrame.size() < frameCount) {
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    currentFrame.unshift(data.get(x));
                    hit = false;
                } else {
                    hit = true;
                }
                candidateValue = Integer.parseInt(currentFrame.get(currentFrame.size() - 1).toString());
            } else {
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    //candidateValue = Integer.parseInt(currentFrame.get(candidate).toString());
                    //currentFrame.set(candidate, data.get(x));
                    currentFrame.unshift(data.get(x));
                    candidateValue = Integer.parseInt(currentFrame.pop().toString());
                    candidate++;

                    if (candidate > (frameCount - 1)) {
                        candidate = 0;
                    }

                    hit = false;
                } else {
                    candidateValue = Integer.parseInt(currentFrame.end().toString());
                    hit = true;
                }
            }
            //-------------
            frames.add(new MemoryFrame((x + 1), this.currentFrame, candidateValue, hit));
        }//end of loop

    }

    public void lru_Counter() {
        algorithm_id = 3;
        currentFrame = new JSarray();
        frames = new ArrayList<MemoryFrame>();
        int candidateValue = 0;
        JSarray currentFrameCandidate = new JSarray();
        boolean hit = false;

        for (int x = 0; x < data.size(); x++) {
            if (currentFrame.size() < frameCount) {
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    currentFrame.push(data.get(x));
                    hit = false;
                } else {
                    hit = true;
                }
                candidateValue = Integer.parseInt(currentFrame.get(0).toString());
            } else {
                //---------------------------------------------LRU
                JSarray tempFrame = new JSarray();
                for (int y = 0; y <= x; y++) {
                    tempFrame.push(data.get(y));
                }
                tempFrame.reverse();
                currentFrameCandidate = new JSarray();
                for (int a = 0; a < currentFrame.size(); a++) {
                    currentFrameCandidate.push(tempFrame.indexOf(currentFrame.get(a)));
                }
                Collections.sort(currentFrameCandidate);
                currentFrameCandidate.reverse();
                int a = Integer.parseInt(currentFrameCandidate.get(0).toString());
                candidateValue = Integer.parseInt(tempFrame.get(a).toString());
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    for (int j = 0; j < currentFrame.size(); j++) {
                        if (Integer.parseInt(currentFrame.get(j).toString()) == candidateValue) {
                            currentFrame.set(j, data.get(x));
                        }
                    }
                    hit = false;
                } else {
                    hit = true;
                }
            }
            frames.add(new MemoryFrame((x + 1), this.currentFrame, candidateValue, hit));
        }//end of loop

    }//end of function	

    public void lru_Stack() {
        algorithm_id = 4;
        currentFrame = new JSarray();
        frames = new ArrayList<MemoryFrame>();
        int candidateValue = 0;
        JSarray currentFrameCandidate = new JSarray();
        boolean hit = false;

        for (int x = 0; x < data.size(); x++) {
            if (currentFrame.size() < frameCount) {
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    currentFrame.unshift(data.get(x));
                    hit = false;
                } else {
                    hit = true;
                }
                candidateValue = Integer.parseInt(currentFrame.get(currentFrame.size() - 1).toString());
            } else {
                JSarray tempFrame = new JSarray();
                for (int y = 0; y <= x; y++) {
                    tempFrame.push(data.get(y));
                }
                tempFrame.reverse();
                currentFrameCandidate = new JSarray();
                for (int a = 0; a < currentFrame.size(); a++) {
                    currentFrameCandidate.push(tempFrame.indexOf(currentFrame.get(a)));
                }
                Collections.sort(currentFrameCandidate);
                currentFrameCandidate.reverse();
                int www = Integer.parseInt(currentFrameCandidate.get(0).toString());
                candidateValue = Integer.parseInt(tempFrame.get(www).toString());
                //-----------------------------------------------------
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    for (int j = 0; j < currentFrame.size(); j++) {
                        if (Integer.parseInt(currentFrame.get(j).toString()) == candidateValue) {
                            currentFrame.remove(j);
                        }
                    }
                    currentFrame.unshift(data.get(x));
                    hit = false;
                } else {
                    for (int j = 0; j < currentFrame.size(); j++) {
                        if (Integer.parseInt(currentFrame.get(j).toString()) == Integer.parseInt(data.get(x).toString())) {
                            currentFrame.remove(j);
                        }
                    }
                    currentFrame.unshift(data.get(x));

                    hit = true;
                }
            }
            frames.add(new MemoryFrame((x + 1), this.currentFrame, candidateValue, hit));
        }//end of loop
    }//end of function	

    public void optimal_page_replacement() {
        algorithm_id = 5;
        currentFrame = new JSarray();
        frames = new ArrayList<MemoryFrame>();
        int candidateValue = 0;
        JSarray currentFrameCandidate = new JSarray();
        boolean hit = false;

        for (int x = 0; x < data.size(); x++) {
            if (currentFrame.size() < frameCount) {
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    currentFrame.push(data.get(x));
                    hit = false;
                } else {
                    hit = true;
                }
                candidateValue = Integer.parseInt(currentFrame.get(0).toString());
            } else {
                JSarray tempFrame = new JSarray();

                for (int y = x; y < data.size(); y++) {
                    tempFrame.push(data.get(y));
                }
                JSarray outofscope = new JSarray();
                currentFrameCandidate = new JSarray();
                for (int a = 0; a < currentFrame.size(); a++) {
                    currentFrameCandidate.push(tempFrame.indexOf(currentFrame.get(a)));
                    if (Integer.parseInt(currentFrameCandidate.get(a).toString()) == -1) {
                        outofscope.push(a);
                    }
                }
                if (outofscope.size() == 1) {
                    int ccc = Integer.parseInt(outofscope.get(0).toString());
                    candidateValue = Integer.parseInt(currentFrame.get(ccc).toString());

                } else if (outofscope.size() == 0) {
                    Collections.sort(currentFrameCandidate);
                    currentFrameCandidate.reverse();
                    int jjj = Integer.parseInt(currentFrameCandidate.get(0).toString());
                    candidateValue = Integer.parseInt(tempFrame.get(jjj).toString());
                } else if (outofscope.size() >= 2) {
                    JSarray myTemp = new JSarray();
                    for (int mahal = 0; mahal <= x; mahal++) {
                        myTemp.push(data.get(mahal));
                    }
                    myTemp.reverse();
                    JSarray lruCandidate = new JSarray();
                    for (int melvin = 0; melvin < outofscope.size(); melvin++) {
                        int hays = Integer.parseInt(outofscope.get(melvin).toString());
                        lruCandidate.push(myTemp.indexOf(currentFrame.get(hays)));
                    }
                    Collections.sort(lruCandidate);
                    lruCandidate.reverse();

                    candidateValue = Integer.parseInt(myTemp.get(Integer.parseInt(lruCandidate.get(0).toString())).toString());
                    ///--------------------------------------------------
                }
                if (currentFrame.indexOf(data.get(x)) == -1) {
                    for (int jel = 0; jel < currentFrame.size(); jel++) {
                        if (Integer.parseInt(currentFrame.get(jel).toString()) == candidateValue) {
                            currentFrame.set(jel, data.get(x));
                        }
                    }

                    hit = false;
                } else {
                    hit = true;
                }
            }
            frames.add(new MemoryFrame((x + 1), this.currentFrame, candidateValue, hit));
        }//end of loop
    }//end of function	

}
