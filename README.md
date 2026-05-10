# 摄影师个人博客

这是一个零成本摄影博客方案：Hugo Extended 负责生成静态网站，GitHub Pages 免费托管，`hugo-theme-gallery` 负责相册瀑布流和灯箱预览。

## 本地预览

如果命令行已经能识别 `hugo`：

```powershell
hugo server -D
```

如果提示找不到 `hugo`，本机 Winget 安装路径可以这样运行：

```powershell
& "$env:LOCALAPPDATA\Microsoft\WinGet\Packages\Hugo.Hugo.Extended_Microsoft.Winget.Source_8wekyb3d8bbwe\hugo.exe" server -D
```

浏览器打开 `http://127.0.0.1:1313/`。

## 添加一个相册

可以直接复制现有 `content/storm-light/`，也可以用模板生成：

```powershell
hugo new --kind album tokyo-night/index.md
```

每个相册是一个文件夹，里面放 `index.md` 和公开展示用的 JPG：

```plain
content/
  tokyo-night/
    index.md
    feature.jpg
    street-01.jpg
    street-02.jpg
```

`index.md` 示例：

```yaml
---
title: "东京夜行"
date: 2026-05-10T20:00:00+09:00
description: "夜色、霓虹和雨后的街道。"
categories: ["街拍"]
params:
  theme: "dark"
resources:
  - src: feature.jpg
    params:
      cover: true
---

这里写拍摄记录、器材、路线、后期思路。
```

## 照片数据策略

- `photos_raw/`：放 RAW、原图、高分辨率导出，默认不上传 Git。
- `content/<album>/`：只放准备公开展示的 JPG。
- 首页设置了 `publishResources: false`，构建时会尽量只发布 Hugo 生成的网页图片，减少体积并避免公开原始文件。
- 不建议使用 WebP 作为源图，gallery 主题文档说明 Hugo 的 Go WebP 处理可能导致缩略图发灰。

## 发布到 GitHub Pages

1. 在 GitHub 创建一个新仓库。
2. 在本地添加远程仓库：

```powershell
& "C:\Program Files\Git\cmd\git.exe" remote add origin https://github.com/<你的用户名>/<仓库名>.git
```

3. 提交并推送：

```powershell
& "C:\Program Files\Git\cmd\git.exe" add .
& "C:\Program Files\Git\cmd\git.exe" commit -m "Create photography blog"
& "C:\Program Files\Git\cmd\git.exe" push -u origin main
```

4. 到 GitHub 仓库的 Settings > Pages，把 Source 设置为 GitHub Actions。

之后每次推送，`.github/workflows/hugo.yaml` 会自动构建并发布。

部署工作流会自动使用 GitHub Pages 提供的真实地址作为 `baseURL`。只有你不用这个工作流、改成手动构建时，才需要把 `hugo.toml` 里的 `baseURL` 改成公开站点地址。
