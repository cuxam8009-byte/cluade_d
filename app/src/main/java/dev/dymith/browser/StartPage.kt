package dev.dymith.browser

/** Trang khởi động cục bộ: nền cực quang nhạt để thấy rõ hiệu ứng kính phía sau thanh công cụ. */
object StartPage {
    const val URL = "https://start.dymith.invalid/"

    val HTML: String = """
<!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">
<meta name="color-scheme" content="light">
<title>Dymith</title>
<style>
  :root {
    --ink: #0e1530;
    --ink-soft: rgba(14, 21, 48, .6);
    --glass: rgba(255, 255, 255, .56);
    --rim: rgba(255, 255, 255, .8);
    --lift: 0 12px 32px rgba(52, 64, 140, .14);
  }
  * { box-sizing: border-box; -webkit-tap-highlight-color: transparent; }
  html, body { margin: 0; }
  body {
    min-height: 100vh;
    padding: 34px 18px 170px;
    font: 16px/1.45 -apple-system, "SF Pro Text", system-ui, Roboto, "Noto Sans", sans-serif;
    color: var(--ink);
    background:
      radial-gradient(70% 38% at 10% 4%, #b9d0ff 0, rgba(185, 208, 255, 0) 70%),
      radial-gradient(60% 34% at 96% 16%, #e6c8ff 0, rgba(230, 200, 255, 0) 70%),
      radial-gradient(80% 40% at 60% 96%, #ffd6c2 0, rgba(255, 214, 194, 0) 70%),
      #f3f5fc;
    background-attachment: fixed;
  }
  h1 { margin: 6px 4px 0; font-size: 44px; line-height: 1.05; font-weight: 600; letter-spacing: -.035em; }
  .lede { margin: 8px 4px 0; color: var(--ink-soft); }
  h2 { margin: 30px 6px 10px; font-size: 15px; font-weight: 600; color: var(--ink-soft); }
  .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
  .card {
    display: block; padding: 14px; color: inherit; text-decoration: none;
    background: var(--glass); border: 1px solid var(--rim); border-radius: 22px;
    -webkit-backdrop-filter: blur(22px) saturate(1.6); backdrop-filter: blur(22px) saturate(1.6);
    box-shadow: var(--lift);
  }
  .card:active { transform: scale(.98); }
  .card b { display: block; font-weight: 600; }
  .card small { display: block; margin-top: 2px; color: var(--ink-soft); font-size: 13px; line-height: 1.35; }
  .chip { width: 34px; height: 34px; margin-bottom: 12px; border-radius: 50%; display: grid; place-items: center; color: #fff; font-weight: 600; }
  .c1 { background: linear-gradient(135deg, #5b9bff, #6d5cff); }
  .c2 { background: linear-gradient(135deg, #ff7a7a, #ff4f8b); }
  .c3 { background: linear-gradient(135deg, #3ccf9a, #1fa7c9); }
  .c4 { background: linear-gradient(135deg, #ffb457, #ff7a45); }
  input {
    width: 100%; margin-top: 10px; padding: 12px 14px; font: inherit; color: var(--ink);
    border: 1px solid rgba(14, 21, 48, .12); border-radius: 14px; background: rgba(255, 255, 255, .82); outline: none;
  }
  input:focus { border-color: #6d5cff; }
</style>
</head>
<body>
  <h1>Dymith</h1>
  <p class="lede">Bản thử Phase 0. Lõi Rust, giao diện kính.</p>

  <h2>Mở nhanh</h2>
  <div class="grid">
    <a class="card" href="https://www.google.com"><div class="chip c1">G</div><b>Google</b><small>Tìm kiếm</small></a>
    <a class="card" href="https://m.youtube.com"><div class="chip c2">Y</div><b>YouTube</b><small>Video và cuộn mượt</small></a>
    <a class="card" href="https://vi.wikipedia.org/wiki/Vi%E1%BB%87t_Nam"><div class="chip c3">W</div><b>Wikipedia</b><small>Bài dài để thử cuộn</small></a>
    <a class="card" href="https://vnexpress.net"><div class="chip c4">V</div><b>VnExpress</b><small>Trang tin nhiều quảng cáo</small></a>
  </div>

  <h2>Bài thử của Phase 0</h2>
  <div class="grid">
    <a class="card" href="https://accounts.google.com"><b>Đăng nhập Google</b><small>Xem có bị chặn không</small></a>
    <a class="card" href="https://browserbench.org/"><b>Đo tốc độ</b><small>Speedometer, MotionMark</small></a>
    <a class="card" href="https://d3ward.github.io/toolz/adblock.html"><b>Thử chặn quảng cáo</b><small>Xem số quảng cáo bị chặn</small></a>
    <div class="card"><b>Gõ tiếng Việt</b><small>Thử Telex hoặc VNI</small><input type="text" placeholder="Gõ thử ở đây" autocomplete="off"></div>
  </div>
</body>
</html>
""".trimIndent()
}
