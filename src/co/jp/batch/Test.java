package co.jp.batch;



/**
 * Test.
 *
 * @author Dulk
 * @version 20170612
 * @date 2017/6/12
 */
public class Test {
    public static void main(String[] args) {
        String temp = "zhangsan:123";
        String[] arr1 = temp.split(":", 0);
        String[] arr2 = temp.split(":");

        for (String bean : arr2) {
            System.out.println(bean);
        }

    }
    /*
    todo
    http://www.opencai.net/apifree/
    1、(done)大乐透的彩票生成规则需要修改，后两位可以和前面重复？
    2、兑奖方式需要根据不同奖项的兑奖规则进行更改
    3、(done)写checker
    4、generater需要根据不同彩票修改
     */
}
