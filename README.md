# PieChartFix

![v1.0.0截图](https://raw.githubusercontent.com/lijingnan/PieChartFix/master/img/img_01.png)

#使用方法：
###1、依赖配置
在项目最外面的build.gradle文件中，allprojects节点下的repositories中添加：
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
再在app的build.gradle文件中，dependencies节点下添加：
```
dependencies {
	implementation 'com.github.lijingnan:PieChartFix:v1.0.0'
	implementation 'com.github.PhilJay:MPAndroidChart:v3.0.3'
}
```

###2、代码中使用
```
<com.github.lijingnan.piechartfix.PieChartFixCover
     android:id="@+id/pie_chart_fix"
     android:layout_width="match_parent"
     android:layout_height="250dp"
     android:layout_marginTop="10dp"
     app:chart_auto_adapt_text_size="true" />
```
和原生的PieChart用法相同，只是多了一个chart_auto_adapt_text_size，需在xml中设置： 使用了自定义属性，注意在根节点内添加：xmlns:app="http://schemas.android.com/apk/res-auto"

###3、说明
v1.0.0版本是在MPAndroidChart版本为v3.0.3的基础上进行开发的 使用时，值的显示位置，请仅设置：mDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//值显示的位置