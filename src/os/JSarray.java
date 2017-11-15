package os;

import java.util.ArrayList;

public class JSarray extends ArrayList {

    public JSarray() {
        //construct
    }

    private ArrayList js = new ArrayList();

    public void reverse() {
        js = new JSarray();
        for (Object o : this) {
            js.add(o);
        }
        for (int x = (js.size() - 1), y = 0; y < js.size(); x--, y++) {
            this.set(y, js.get(x));
        }
    }

    public Object end() {
        Object popout = this.get(this.size() - 1);
        return popout;
    }

    public Object pop() {
        Object popout = this.get(this.size() - 1);
        this.remove(this.size() - 1);
        return popout;
    }

    public Object setExact(int loc, Object o) {
        Object popout = this.get(loc);
        this.set(loc, o);
        return popout;
    }

    public void unshift(Object o) {
        this.add(0, o);
    }

    public void push(Object o) {
        this.add(o);
    }

}
