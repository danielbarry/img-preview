# Img Preview

A tool for creating interesting low disk-space/bandwidth image previews.

## Methods

The following are some examples of the output that can be expected.

### Scale

This is simply a way of making a large image into a smaller image preview, with
some examples below:

|Speed |Image                        |Size|Ratio|Description              |
|:----:|:---------------------------:|:--:|:---:|:------------------------|
|Fast  |![](doc/scale_128_fast.jpg)  |5089|0.045|Standard Java scaling    |
|Normal|![](doc/scale_128_normal.jpg)|4939|0.044|Java render hints enabled|
|Slow  |![](doc/scale_128_slow.jpg)  |4011|0.035|Progressive scaling      |

### Original

This is the original image for reference (size: `113457`):

![](doc/original.jpg)
