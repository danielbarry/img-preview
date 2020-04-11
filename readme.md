# Img Preview

A tool for creating interesting low disk-space/bandwidth image previews.

## Methods

![Original image to be processed](doc/original.jpg)

### Scale

This is simply a way of making a large image into a smaller image preview, with
some examples below:

|Speed |Image                        |Description              |
|:----:|:---------------------------:|:------------------------|
|Fast  |![](doc/scale_128_fast.jpg)  |Standard Java scaling    |
|Normal|![](doc/scale_128_normal.jpg)|Java render hints enabled|
|Slow  |![](doc/scale_128_slow.jpg)  |Progressive scaling      |
