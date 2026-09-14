package win.everything.solution.removestring;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class RemoveStringTest {


        @Test
        public void testSomething() {
            RemoveString removeString = new RemoveString();
            assertEquals("8j8mBliB8gimjB8B8jlB", removeString.noSpace("8 j 8   mBliB8g  imjB8B8  jl  B"));
            assertEquals("88Bifk8hB8BB8BBBB888chl8BhBfd", removeString.noSpace("8 8 Bi fk8h B 8 BB8B B B  B888 c hl8 BhB fd"));
            assertEquals("8aaaaaddddr", removeString.noSpace("8aaaaa dddd r     "));
            assertEquals("jfBmgklf8hg88lbe8", removeString.noSpace("jfBm  gk lf8hg  88lbe8 "));
            assertEquals("8jaam", removeString.noSpace("8j aam"));
        }


}