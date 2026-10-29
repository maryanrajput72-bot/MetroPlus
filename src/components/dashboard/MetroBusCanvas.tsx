import React, { useRef, useEffect } from 'react';

interface MetroBusCanvasProps {
  stationName: string;
}

export const MetroBusCanvas: React.FC<MetroBusCanvasProps> = ({ stationName }) => {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const busImageRef = useRef<HTMLImageElement | null>(null);

  useEffect(() => {
    const img = new Image();
    img.src = '/metro-bus.png';
    img.onload = () => {
      busImageRef.current = img;
      renderBus();
    };
  }, []);

  const renderBus = () => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const img = busImageRef.current;
    if (!img) return;

    // Fixed internal canvas resolution matching original 1024x512
    const w = 1024;
    const h = 512;
    if (canvas.width !== w || canvas.height !== h) {
      canvas.width = w;
      canvas.height = h;
    }

    ctx.clearRect(0, 0, w, h);

    // 1. Draw base authentic bus graphic (clean transparent background, clean tinted glass, clean bumper)
    ctx.drawImage(img, 0, 0, w, h);

    // 2. Draw destination board amber LED text directly onto the full windshield display
    ctx.save();
    // True center of the full destination screen on the bus windshield (X: 465 to 845)
    const centerX = 655;
    const centerY = 104;
    ctx.translate(centerX, centerY);
    // Subtle tilt matching windshield slope (-1.0 degree)
    ctx.rotate((-1.0 * Math.PI) / 180);

    // Dynamic sizing based on station name length
    const nameUpper = stationName.toUpperCase();
    let fontSize = 28;
    if (nameUpper.length > 15) {
      fontSize = 21;
    } else if (nameUpper.length > 11) {
      fontSize = 25;
    }

    ctx.font = `900 ${fontSize}px "Courier New", monospace, sans-serif`;
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    // Amber LED outer phosphor bloom
    ctx.shadowColor = 'rgba(255, 140, 0, 0.9)';
    ctx.shadowBlur = 12;
    ctx.fillStyle = '#FF9E00';
    ctx.fillText(nameUpper, 0, 0);

    // Secondary intense phosphor glow
    ctx.shadowBlur = 5;
    ctx.fillStyle = '#FFB814';
    ctx.fillText(nameUpper, 0, 0);

    // Crisp high-luminance LED dot center
    ctx.shadowBlur = 0;
    ctx.fillStyle = '#FFF59D';
    ctx.fillText(nameUpper, 0, 0);

    ctx.restore();

    // 3. Realistic Windshield Glass Sheen (Drawn OVER the LEDs to look optically behind the glass across the full screen)
    ctx.save();
    const glassGrad = ctx.createLinearGradient(465, 75, 845, 135);
    glassGrad.addColorStop(0, 'rgba(255, 255, 255, 0.16)');
    glassGrad.addColorStop(0.35, 'rgba(255, 255, 255, 0.03)');
    glassGrad.addColorStop(0.7, 'rgba(255, 255, 255, 0.07)');
    glassGrad.addColorStop(1, 'rgba(255, 255, 255, 0.01)');
    ctx.fillStyle = glassGrad;
    ctx.fillRect(465, 76, 380, 56);
    ctx.restore();
  };

  useEffect(() => {
    if (busImageRef.current) {
      renderBus();
    }
  }, [stationName]);

  return (
    <canvas
      ref={canvasRef}
      className="w-full h-auto object-contain drop-shadow-[0_20px_32px_rgba(201,162,74,0.35)] transition-transform duration-500 group-hover:scale-105 select-none pointer-events-none"
      style={{
        aspectRatio: '1024 / 512',
      }}
    />
  );
};
